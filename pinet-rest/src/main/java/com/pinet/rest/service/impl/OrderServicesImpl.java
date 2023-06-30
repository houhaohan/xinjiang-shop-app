package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.entity.enums.MemberLevelEnum;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.core.constants.DB;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.IPUtils;
import com.pinet.core.util.LatAndLngUtils;
import com.pinet.core.util.SpringContextUtils;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.*;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.entity.param.OrderRefundNotifyParam;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.param.RefundParam;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Service
@DS(DB.MASTER)
public class OrderServicesImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {
    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private IOrderProductService orderProductService;

    @Resource
    private IOrderAddressService orderAddressService;

    @Resource
    private IShopService shopService;

    @Resource
    private ICartService cartService;

    @Resource
    private IShopProductSpecService shopProductSpecService;

    @Resource
    private JmsUtil jmsUtil;

    @Resource
    private IOrderPayService orderPayService;

    @Resource
    private IOrderProductSpecService orderProductSpecService;

    @Resource
    private IOrderRefundService orderRefundService;

    @Resource
    private ICustomerMemberService customerMemberService;

    @Override
    public List<OrderListVo> orderList(OrderListDto dto) {

        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Page<OrderListVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        IPage<OrderListVo> orderListVos = ordersMapper.selectOrderList(page, customerId);
        orderListVos.getRecords().forEach(k -> {
            k.setOrderStatusStr(OrderStatusEnum.getEnumByCode(k.getOrderStatus()));
            //如果是自提订单并且是配送中 修改状态状态str为可领取
            if (k.getOrderStatus().equals(OrderStatusEnum.SEND_OUT.getCode()) && k.getOrderType() == 2) {
                k.setOrderStatusStr("可领取");
            }
            List<OrderProduct> orderProducts = orderProductService.getByOrderId(k.getOrderId());
            k.setOrderProducts(orderProducts);
            k.setProdNum(orderProducts.size());
        });
        return orderListVos.getRecords();
    }

    @Override
    public OrderDetailVo orderDetail(Long orderId) {
        OrderDetailVo orderDetailVo = ordersMapper.selectOrderDetail(orderId);
        if (orderDetailVo == null) {
            throw new PinetException("订单不存在");
        }
        orderDetailVo.setOrderProducts(orderProductService.getByOrderId(orderId));
        //判断是自提还是外卖
        if (orderDetailVo.getOrderType() == 1) {
            OrderAddress orderAddress = orderAddressService.getOrderAddress(orderId);
            orderDetailVo.setAddress(orderAddress.getAddress());
            //脱敏
            orderDetailVo.setName(DesensitizedUtil.chineseName(orderAddress.getName()));
            orderDetailVo.setTel(DesensitizedUtil.mobilePhone(orderAddress.getTel()));
        }

        Shop shop = shopService.getById(orderDetailVo.getShopId());
        orderDetailVo.setShop(shop);


        //处理下预计送达时间
        String estimateArrivalStartTime = DateUtil.format(orderDetailVo.getEstimateArrivalStartTime(), "HH:mm");
        String estimateArrivalEndTime = DateUtil.format(orderDetailVo.getEstimateArrivalEndTime(), "HH:mm");

        orderDetailVo.setEstimateArrivalTime(estimateArrivalStartTime + "-" + estimateArrivalEndTime);


        //订单过期时间
        orderDetailVo.setExpireTime(getExpireTime(orderDetailVo.getCreateTime()));

        Integer prodTotalNum = orderDetailVo.getOrderProducts().stream().map(OrderProduct::getProdNum).reduce(Integer::sum).orElse(0);
        orderDetailVo.setProdTotalNum(prodTotalNum);
        return orderDetailVo;
    }

    @Override
    public OrderSettlementVo orderSettlement(OrderSettlementDto dto) {

        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();

        Shop shop = shopService.getById(dto.getShopId());
        OrderSettlementVo vo = new OrderSettlementVo();
        vo.setShopName(shop.getShopName());
        //配送费默认4元 自提没有配送费
        BigDecimal shippingFee = getShippingFee(dto.getOrderType());

        vo.setShippingFee(shippingFee);

        List<OrderProduct> orderProducts = new ArrayList<>();
        if (dto.getSettlementType() == 1) {
            //购物车结算（通过店铺id 查找购物车进行结算）
            orderProducts = orderProductService.getByCartAndShop(dto.getShopId());
        } else {
            //直接结算（通过具体的商品样式、商品数量进行结算）
            List<Long> shopProdSpecIds = splitShopProdSpecIds(dto.getShopProdSpecIds());
            QueryOrderProductBo query = new QueryOrderProductBo(dto.getShopProdId(), dto.getProdNum(), shopProdSpecIds);
            OrderProduct orderProduct = orderProductService.getByQueryOrderProductBo(query);
            orderProducts.add(orderProduct);
        }

        vo.setOrderProductBoList(orderProducts);

        //判断店铺是否营业
        if (!shopService.checkShopStatus(shop)) {
            throw new PinetException("店铺已经打烊了~");
        }

        vo.setOrderMakeCount(countShopOrderMakeNum(dto.getShopId()));
        //计算商品总金额
        BigDecimal orderProdPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //设置订单原价
        vo.setOriginalPrice(orderProdPrice.add(shippingFee));


        //计算商品折后金额
        orderProdPrice = getDiscountedPrice(customerId, orderProdPrice);

        //计算订单优惠后现价
        BigDecimal orderPrice = orderProdPrice.add(shippingFee);
        vo.setOrderPrice(orderPrice);

        //返回预计送达时间
        Date now = new Date();
        String estimateArrivalStartTime = DateUtil.format(DateUtil.offsetHour(now, 1), "HH:mm");
        String estimateArrivalEndTime = DateUtil.format(DateUtil.offsetMinute(now, 90), "HH:mm");

        vo.setEstimateArrivalTime(estimateArrivalStartTime + "-" + estimateArrivalEndTime);


        Integer orderProductNum = orderProducts.stream().map(OrderProduct::getProdNum).reduce(Integer::sum).orElse(0);
        vo.setOrderProductNum(orderProductNum);


        return vo;
    }

    @Override
    public Integer countShopOrderMakeNum(Long shopId) {
        Date date = new Date();
        Date queryDate = DateUtil.offsetHour(date, -8);
        return ordersMapper.countShopOrderMakeNum(shopId, queryDate);
    }

    @Override
    @DSTransactional
    public CreateOrderVo createOrder(CreateOrderDto dto) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();

        Shop shop = shopService.getById(dto.getShopId());
        //判断店铺是否营业
        if (!shopService.checkShopStatus(shop)) {
            throw new PinetException("店铺已经打烊了~");
        }

        //配送费
        BigDecimal shippingFee = getShippingFee(dto.getOrderType());

        //自提订单默认距离是0  外卖订单 校验距离 10公里以内
        double m = 0D;
        if (dto.getOrderType() == 1) {
            m = LatAndLngUtils.getDistance(Double.parseDouble(dto.getLng()), Double.parseDouble(dto.getLat()),
                    Double.parseDouble(shop.getLng()), Double.parseDouble(shop.getLat()));
            if (m > 10000D) {
                throw new PinetException("店铺距离过远,无法配送");
            }
        }


        List<OrderProduct> orderProducts = new ArrayList<>();
        if (dto.getSettlementType() == 1) {
            //购物车结算（通过店铺id 查找购物车进行结算）
            orderProducts = orderProductService.getByCartAndShop(dto.getShopId());

            //删除购物车已购商品
            cartService.delCartByShopId(dto.getShopId(), userId);


        } else {
            //直接结算（通过具体的商品样式、商品数量进行结算）
            List<Long> shopProdSpecIds = splitShopProdSpecIds(dto.getShopProdSpecIds());
            QueryOrderProductBo query = new QueryOrderProductBo(dto.getShopProdId(), dto.getProdNum(), shopProdSpecIds);
            OrderProduct orderProduct = orderProductService.getByQueryOrderProductBo(query);
            orderProducts.add(orderProduct);
        }

        //减少已购商品的库存(第一版暂不加锁 后期考虑加乐观锁或redis锁)
        for (OrderProduct orderProduct : orderProducts) {
            for (OrderProductSpec orderProductSpec : orderProduct.getOrderProductSpecs()) {
                int res = shopProductSpecService.reduceStock(orderProductSpec.getShopProdSpecId(), orderProduct.getProdNum());
                if (res != 1) {
                    throw new PinetException("库存更新失败");
                }
            }
        }


        //订单商品原价
        BigDecimal orderProdOriginalPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        //商品折后价
        BigDecimal orderProdPrice = getDiscountedPrice(userId, orderProdOriginalPrice);

        //优惠金额
        BigDecimal discountAmount = orderProdOriginalPrice.subtract(orderProdPrice);
        //总金额
        BigDecimal orderPrice = orderProdPrice.add(shippingFee);

        //对比订单总金额和结算的总金额  如果不相同说明商品价格有调整
        if (orderPrice.compareTo(dto.getOrderPrice()) != 0) {
            throw new PinetException("订单信息发生变化,请重新下单");
        }

        //判断该订单是否有佣金
        //邀请人必须是店帮主  被邀人不能是店帮主

        //创建订单基础信息
        Orders order = createOrder(dto, shippingFee, m, orderPrice, orderProdPrice, discountAmount, shop);
        setOrdersCommission(order,orderProducts);
        //插入订单
        this.save(order);

        //插入订单商品  并设置订单号
        orderProducts.forEach(k -> {
            k.setOrderId(order.getId());
            //保存订单商品表
            orderProductService.save(k);

            //保存订单商品样式表
            k.getOrderProductSpecs().forEach(k1 -> {
                k1.setOrderProdId(k.getId());
                k1.setOrderId(order.getId());
            });
            orderProductSpecService.saveBatch(k.getOrderProductSpecs());
        });

        //外卖订单插入订单地址表
        if (dto.getOrderType() == 1) {
            OrderAddress orderAddress = orderAddressService.createByCustomerAddressId(dto.getCustomerAddressId());
            orderAddress.setOrderId(order.getId());
            orderAddressService.save(orderAddress);
        }

        //将订单放到mq中
        jmsUtil.delaySend(QueueConstants.QING_SHI_ORDER_PAY_NAME, order.getId().toString(), (long) (15 * 60 * 1000));


        CreateOrderVo createOrderVo = new CreateOrderVo();
        createOrderVo.setOrderId(order.getId());
        createOrderVo.setOrderNo(order.getOrderNo());
        createOrderVo.setOrderPrice(orderPrice);
        //返回订单过期时间
        createOrderVo.setExpireTime(getExpireTime(order.getCreateTime()));

        return createOrderVo;
    }

    /**
     * 设置佣金
     *
     * @param orders
     */
    private void setOrdersCommission(Orders orders,List<OrderProduct> orderProducts) {
        if (orders.getShareId() <= 0){
            return;
        }

        //判断下单人和分享人是否是同一个人
        if (orders.getCustomerId().equals(orders.getShareId())){
            return;
        }

        //下单人会员等级
        Integer customerMemberLevel = customerMemberService.getMemberLevel(orders.getCustomerId());
        //分享人会员等级
        Integer shareMemberLevel = customerMemberService.getMemberLevel(orders.getShareId());

        //邀请人必须是店帮主  被邀人不能是店帮主
        if (shareMemberLevel.equals(MemberLevelEnum._20.getCode()) && !customerMemberLevel.equals(MemberLevelEnum._20.getCode())){
            //佣金=商品总金额 * 0.1
            BigDecimal commission = orders.getOrderProdPrice().multiply(new BigDecimal("0.1")).setScale(2, RoundingMode.HALF_UP);
            orders.setCommission(commission);


            //设置单个商品的佣金
            orderProducts.forEach(k->{
                k.setCommission(k.getProdPrice().multiply(new BigDecimal("0.1")).setScale(2, RoundingMode.HALF_UP));
            });

        }
    }


    private Long getExpireTime(Date createTime) {
        Date expireTime = DateUtil.offsetMinute(createTime, 15);
        return expireTime.getTime();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object orderPay(OrderPayDto dto) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();

        Orders orders = getById(dto.getOrderId());
        if (orders == null) {
            throw new PinetException("订单不存在");
        }

        if (orders.getOrderPrice().compareTo(dto.getOrderPrice()) != 0) {
            throw new PinetException("支付金额异常,请重新支付");
        }

        //根据不同支付渠道获取调用不同支付方法
        IPayService payService = SpringContextUtils.getBean(dto.getChannelId() + "_" + "service", IPayService.class);
        //封装PayParam
        PayParam param = new PayParam();
        param.setOpenId(dto.getOpenId());
        param.setOrderNo(orders.getOrderNo().toString());
        param.setPayPrice(dto.getOrderPrice());
        param.setPayDesc("轻食订单下单");
        param.setPayType(1);
        Object res = payService.pay(param);


        //构造orderPay
        OrderPay orderPay = new OrderPay();
        orderPay.setOrderId(orders.getId());
        orderPay.setOrderNo(orders.getOrderNo());
        orderPay.setCustomerId(customerId);
        orderPay.setPayStatus(1);
        orderPay.setOrderPrice(orders.getOrderPrice());
        orderPay.setPayPrice(dto.getOrderPrice());
        orderPay.setOpenId(dto.getOpenId());
        orderPay.setChannelId(dto.getChannelId());
        orderPay.setPayName(payService.getPayName());
        orderPay.setIp(IPUtils.getIpAddr());

        orderPayService.save(orderPay);

        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean orderPayNotify(OrderPayNotifyParam param) {
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getOrderNo, param.getOrderNo()).eq(BaseEntity::getDelFlag, 0);
        Orders orders = getOne(lambdaQueryWrapper);
        if (orders == null) {
            log.error("微信支付回调出现异常,订单号不存在:" + param.getOrderNo());
            return false;
        }


        OrderPay orderPay = orderPayService.getByOrderIdAndChannelId(orders.getId(), param.getChannelId());
        orderPay.setPayStatus(2);
        orderPay.setPayTime(param.getPayTime());
        orderPay.setOutTradeNo(param.getOutTradeNo());
        orderPayService.updateById(orderPay);

        //判断订单状态  如果订单状态是已取消  就退款
        if (orders.getOrderStatus().equals(OrderStatusEnum.CANCEL.getCode())) {

            IPayService payService = SpringContextUtils.getBean(orderPay.getChannelId() + "_" + "service", IPayService.class);
            //构造退款记录
            Snowflake snowflake = IdUtil.getSnowflake();

            OrderRefund orderRefund = new OrderRefund();
            orderRefund.setRefundNo(snowflake.nextId());
            orderRefund.setOrderId(orders.getId());
            orderRefund.setOrderPayId(orderPay.getId());
            orderRefund.setRefundPrice(orders.getOrderPrice());
            orderRefund.setOrderPrice(orders.getOrderPrice());
            orderRefund.setIsAllRefund(true);
            orderRefund.setRefundDesc("订单超时支付,系统默认退款");
            orderRefund.setRefundStatus(1);
            orderRefundService.save(orderRefund);

            //调用退款方法
            RefundParam refundParam = new RefundParam(orders.getOrderPrice().toString(), orders.getOrderNo().toString(), orderRefund.getRefundNo().toString(), orders.getOrderPrice().toString(), orderRefund.getId());
            payService.refund(refundParam);
            orders.setOrderStatus(OrderStatusEnum.REFUND.getCode());
            return updateById(orders);
        } else {
            //更新总订单状态
            orders.setOrderStatus(OrderStatusEnum.PAY_COMPLETE.getCode());
            return updateById(orders);
        }
    }

    @Override
    public Boolean cancelOrder(Long orderId) {
        Orders orders = getById(orderId);
        if (orders == null) {
            throw new PinetException("订单不存在");
        }
        //判断订单状态  是否可以取消  只有待付款状态可以取消
        if (!orders.getOrderStatus().equals(OrderStatusEnum.NOT_PAY.getCode())) {
            throw new PinetException("只有待付款状态下才可以取消");
        }
        orders.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        return updateById(orders);
    }

    @Override
    public Boolean orderRefundNotify(OrderRefundNotifyParam param) {
        LambdaQueryWrapper<OrderRefund> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderRefund::getRefundNo, param.getRefundNo()).eq(BaseEntity::getDelFlag, 0);
        OrderRefund orderRefund = orderRefundService.getOne(lambdaQueryWrapper);
        if (orderRefund == null) {
            log.error("微信退款回调失败，退款单号不存在" + param.getRefundNo());
            throw new PinetException("退款单号不存在");
        }
        orderRefund.setRefundStatus(2);
        orderRefund.setOutTradeNo(param.getOutTradeNo());
        return orderRefundService.updateById(orderRefund);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recurOrder(Long orderId, Long customerId) {
        Orders order = getById(orderId);
        if (order == null) {
            throw new PinetException("订单不存在");
        }
        List<OrderProduct> orderProducts = orderProductService.getByOrderId(orderId);
        orderProducts.forEach(k -> {
            AddCartDto addCartDto = new AddCartDto();
            addCartDto.setShopId(order.getShopId());
            addCartDto.setShopProdId(k.getShopProdId());
            addCartDto.setProdNum(k.getProdNum());
            addCartDto.setCustomerId(customerId);
            String shopProdSpecIds = k.getOrderProductSpecs().stream().map(OrderProductSpec::getShopProdSpecId).map(String::valueOf).collect(Collectors.joining(","));
            addCartDto.setShopProdSpecIds(shopProdSpecIds);
            cartService.addCart(addCartDto);
        });
    }



    @Override
    public BigDecimal getDiscountedPrice(Long customerId, BigDecimal originalPrice) {
        Integer memberLevel = customerMemberService.getMemberLevel(customerId);
        MemberLevelEnum memberLevelEnum = MemberLevelEnum.getEnumByCode(memberLevel);

        BigDecimal discountedPrice = originalPrice.multiply(memberLevelEnum.getDiscount()).setScale(2, RoundingMode.HALF_UP);

        return discountedPrice;
    }


    private Orders createOrder(CreateOrderDto dto, BigDecimal shippingFee, Double m, BigDecimal orderPrice, BigDecimal orderProdPrice, BigDecimal discountAmount, Shop shop) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        Date now = new Date();
        Date estimateArrivalStartTime = DateUtil.offsetHour(now, 1);
        Date estimateArrivalEndTime = DateUtil.offsetMinute(now, 90);

        Orders order = new Orders();
        Snowflake snowflake = IdUtil.getSnowflake();
        order.setOrderNo(snowflake.nextId());
        order.setOrderType(dto.getOrderType());
        order.setOrderStatus(OrderStatusEnum.NOT_PAY.getCode());
        order.setCustomerId(userId);
        order.setShopId(shop.getId());
        order.setShopName(shop.getShopName());
        order.setOrderPrice(orderPrice);
        order.setOrderProdPrice(orderProdPrice);
        order.setDiscountAmount(discountAmount);
        order.setShippingFee(shippingFee);
        order.setEstimateArrivalStartTime(estimateArrivalStartTime);
        order.setEstimateArrivalEndTime(estimateArrivalEndTime);
        order.setOrderDistance(m.intValue());
        order.setRemark(dto.getRemark());
        order.setShareId(dto.getShareId());

        return order;
    }


    /**
     * 获取配送费
     *
     * @param orderType 订单类型( 1外卖  2自提)
     * @return BigDecimal
     */
    private BigDecimal getShippingFee(Integer orderType) {
        if (orderType == 1) {
            return new BigDecimal("4");
        } else {
            return new BigDecimal("0");
        }
    }

    /**
     * 分割商品样式id
     */
    private List<Long> splitShopProdSpecIds(String shopProdSpecIds) {
        String[] idArray = shopProdSpecIds.split(",");

        return Arrays.stream(idArray)
                .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
    }
}
