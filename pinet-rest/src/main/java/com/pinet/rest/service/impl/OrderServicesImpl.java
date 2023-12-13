package com.pinet.rest.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imdada.open.platform.client.internal.req.order.AddOrderReq;
import com.imdada.open.platform.client.internal.resp.order.AddOrderResp;
import com.imdada.open.platform.exception.RpcException;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.core.constants.DB;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.exception.PinetException;
import com.pinet.core.page.PageRequest;
import com.pinet.core.util.*;
import com.pinet.keruyun.openapi.dto.*;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.OrderDetailVO;
import com.pinet.keruyun.openapi.vo.ScanCodePrePlaceOrderVo;
import com.pinet.keruyun.openapi.vo.TakeoutOrderCreateVo;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.*;
import com.pinet.rest.entity.enums.CapitalFlowStatusEnum;
import com.pinet.rest.entity.enums.CapitalFlowWayEnum;
import com.pinet.rest.entity.enums.MemberLevelEnum;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.entity.param.OrderRefundNotifyParam;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.param.RefundParam;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
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
@Slf4j
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
    private JmsUtil jmsUtil;

    @Resource
    private IOrderPayService orderPayService;

    @Resource
    private IOrderProductSpecService orderProductSpecService;

    @Resource
    private IOrderRefundService orderRefundService;

    @Resource
    private ICustomerMemberService customerMemberService;

    @Resource
    private IBCapitalFlowService bCapitalFlowService;

    @Resource
    private IBUserBalanceService ibUserBalanceService;

    @Resource
    private ICustomerCouponService customerCouponService;

    @Resource
    private IOrderDiscountService orderDiscountService;

    @Autowired
    private IKryApiService kryApiService;

    @Autowired
    private IKryComboGroupDetailService kryComboGroupDetailService;

    @Autowired
    private IKryOrderCompensateService kryOrderCompensateService;

    @Autowired
    private IKryOrderPushLogService kryOrderPushLogService;

    @Resource
    private IDaDaService daDaService;

    @Resource
    private ICustomerAddressService customerAddressService;

    @Value("${spring.profiles.active}")
    private String active;

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
        orderDetailVo.setOrderDiscounts(orderDiscountService.getByOrderId(orderDetailVo.getOrderId()));

        if (StringUtil.isBlank(orderDetailVo.getMealCode()) && StringUtil.isNotBlank(orderDetailVo.getKryOrderNo())) {
            String token = kryApiService.getToken(AuthType.SHOP, orderDetailVo.getKryShopId());
            KryOrderDetailDTO kryOrderDetailDTO = new KryOrderDetailDTO();
            kryOrderDetailDTO.setOrderId(orderDetailVo.getKryOrderNo());
            OrderDetailVO orderDetail = kryApiService.getOrderDetail(orderDetailVo.getKryShopId(), token, kryOrderDetailDTO);
            if (orderDetail != null) {
                Orders orders = new Orders();
                orders.setId(orderId);
                orders.setMealCode(orderDetail.getOrderBaseVO().getSerialNo());
                updateById(orders);
                orderDetailVo.setMealCode(orders.getMealCode());
            }
        }
        return orderDetailVo;
    }

    @Override
    public OrderSettlementVo orderSettlement(OrderSettlementDto dto) {

        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();

        Shop shop = shopService.getById(dto.getShopId());
        if (dto.getOrderType() == 1 && shop.getSupportDelivery() == 0) {
            throw new PinetException("该门店暂不支持外卖");
        }
        OrderSettlementVo vo = new OrderSettlementVo();
        vo.setShopName(shop.getShopName());

        List<OrderProduct> orderProducts = new ArrayList<>();
        if (dto.getSettlementType() == 1) {
            //购物车结算（通过店铺id 查找购物车进行结算）
            orderProducts = orderProductService.getByCartAndShop(dto.getShopId(), dto.getOrderType());
        } else {
            //直接结算（通过具体的商品样式、商品数量进行结算）
            List<Long> shopProdSpecIds = splitShopProdSpecIds(dto.getShopProdSpecIds());
            QueryOrderProductBo query = new QueryOrderProductBo(dto.getShopProdId(), dto.getProdNum(), shopProdSpecIds, dto.getOrderType());
            OrderProduct orderProduct = orderProductService.getByQueryOrderProductBo(query);
            orderProducts.add(orderProduct);
        }

        BigDecimal packageFee = orderProducts.stream().map(OrderProduct::getPackageFee).reduce(BigDecimal.ZERO, BigDecimal::add);
        vo.setPackageFee(packageFee);
        vo.setOrderProductBoList(orderProducts);

        //判断店铺是否营业
        checkShop(shop);

        vo.setOrderMakeCount(countShopOrderMakeNum(dto.getShopId()));
        //计算商品总金额
        BigDecimal orderProdPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);


        //配送费
        BigDecimal shippingFee = getShippingFee(dto.getOrderType(), dto.getCustomerAddressId(), orderProdPrice, shop.getDeliveryShopNo(), shop.getSelfDelivery(),null);
        vo.setShippingFee(shippingFee);

        //设置订单原价 和 商品原价
        vo.setOriginalPrice(orderProdPrice.add(shippingFee).add(packageFee));
        vo.setOriginalOrderProductPrice(orderProdPrice);
        //优惠信息初始化
        List<OrderDiscount> orderDiscounts = new ArrayList<>();

        //使用完优惠券处理
        if (dto.getCustomerCouponId() != null && dto.getCustomerCouponId() > 0) {
            orderProdPrice = processCoupon(dto.getCustomerCouponId(), dto.getShopId(), orderProdPrice, orderDiscounts, 1);
        }

        //店帮主优惠计算
        orderProdPrice = getDiscountedPrice(customerId, orderProdPrice, orderDiscounts);

        //计算订单优惠后现价
        BigDecimal orderPrice = orderProdPrice.add(shippingFee).add(packageFee);
        vo.setOrderPrice(orderPrice);

        //返回预计送达时间
        Date now = new Date();
        String estimateArrivalStartTime = DateUtil.format(DateUtil.offsetMinute(now, 15), "HH:mm");
        String estimateArrivalEndTime = DateUtil.format(DateUtil.offsetMinute(now, 45), "HH:mm");

        vo.setEstimateArrivalTime(estimateArrivalStartTime + "-" + estimateArrivalEndTime);


        Integer orderProductNum = orderProducts.stream().map(OrderProduct::getProdNum).reduce(Integer::sum).orElse(0);
        vo.setOrderProductNum(orderProductNum);
        vo.setOrderDiscounts(orderDiscounts);

        List<CustomerCoupon> customerCoupons = customerCouponService.customerCouponList(new PageRequest(1, 100));
        for (CustomerCoupon customerCoupon : customerCoupons) {
            Boolean isUsable = customerCouponService.checkCoupon(customerCoupon, shop.getId(), vo.getOriginalOrderProductPrice());
            customerCoupon.setIsUsable(isUsable);
        }
        vo.setCustomerCoupons(customerCoupons);
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

        checkShop(shop);
        //自提订单默认距离是0  外卖订单 校验距离 10公里以内
        double distance = 0D;
        if (dto.getOrderType() == 1) {
            if (shop.getSupportDelivery() == 0) {
                throw new PinetException("该店铺暂不支持外卖订单");
            }

            distance = LatAndLngUtils.getDistance(Double.parseDouble(dto.getLng()), Double.parseDouble(dto.getLat()),
                    Double.parseDouble(shop.getLng()), Double.parseDouble(shop.getLat()));
            if (distance > 4000D) {
                throw new PinetException("店铺距离过远,无法配送");
            }
        }


        List<OrderProduct> orderProducts = new ArrayList<>();
        if (dto.getSettlementType() == 1) {
            //购物车结算（通过店铺id 查找购物车进行结算）
            orderProducts = orderProductService.getByCartAndShop(dto.getShopId(), dto.getOrderType());

            //删除购物车已购商品
            cartService.delCartByShopId(dto.getShopId(), userId);


        } else {
            //直接结算（通过具体的商品样式、商品数量进行结算）
            List<Long> shopProdSpecIds = splitShopProdSpecIds(dto.getShopProdSpecIds());
            QueryOrderProductBo query = new QueryOrderProductBo(dto.getShopProdId(), dto.getProdNum(), shopProdSpecIds, dto.getOrderType());
            OrderProduct orderProduct = orderProductService.getByQueryOrderProductBo(query);
            orderProducts.add(orderProduct);
        }

        //减少已购商品的库存(第一版暂不加锁 后期考虑加乐观锁或redis锁)
//        for (OrderProduct orderProduct : orderProducts) {
//            for (OrderProductSpec orderProductSpec : orderProduct.getOrderProductSpecs()) {
//                int res = shopProductSpecService.reduceStock(orderProductSpec.getShopProdSpecId(), orderProduct.getProdNum());
//                if (res != 1) {
//                    throw new PinetException("库存更新失败");
//                }
//            }
//        }

        //计算打包费
        BigDecimal packageFee = orderProducts.stream().map(OrderProduct::getPackageFee).reduce(BigDecimal.ZERO, BigDecimal::add);


        //优惠信息初始化
        List<OrderDiscount> orderDiscounts = new ArrayList<>();

        //订单商品原价
        BigDecimal orderProdOriginalPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);


        //配送费
        BigDecimal shippingFee = getShippingFee(dto.getOrderType(), dto.getCustomerAddressId(), orderProdOriginalPrice, shop.getDeliveryShopNo(), shop.getSelfDelivery(),distance);


        //商品折后价
        BigDecimal orderProdPrice = orderProdOriginalPrice;

        //使用完优惠券处理
        if (dto.getCustomerCouponId() != null && dto.getCustomerCouponId() > 0) {
            orderProdPrice = processCoupon(dto.getCustomerCouponId(), dto.getShopId(), orderProdPrice, orderDiscounts, 2);
        }

        //店帮主
        orderProdPrice = getDiscountedPrice(userId, orderProdPrice, orderDiscounts);


        //优惠金额
        BigDecimal discountAmount = orderProdOriginalPrice.subtract(orderProdPrice);
        //总金额
        BigDecimal orderPrice = orderProdPrice.add(shippingFee).add(packageFee);

        //对比订单总金额和结算的总金额  如果不相同说明商品价格有调整
        if (dto.getOrderSource() != 3 && orderPrice.compareTo(dto.getOrderPrice()) != 0) {
            throw new PinetException("订单信息发生变化,请重新下单");
        }

        //判断该订单是否有佣金
        //邀请人必须是店帮主  被邀人不能是店帮主

        //创建订单基础信息
        Orders order = createOrder(dto, shippingFee, distance, orderPrice, orderProdPrice, discountAmount, shop, packageFee);
        setOrdersCommission(order, orderProducts);
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

        //插入优惠明细表
        if (orderDiscounts.size() > 0) {
            orderDiscounts.forEach(k -> {
                k.setOrderId(order.getId());
            });
            orderDiscountService.saveBatch(orderDiscounts);
        }


        //外卖订单插入订单地址表
        if (dto.getOrderType() == 1) {
            OrderAddress orderAddress = orderAddressService.createByCustomerAddressId(dto.getCustomerAddressId());
            orderAddress.setOrderId(order.getId());
            orderAddressService.save(orderAddress);
        }

        //将订单放到mq中
        jmsUtil.delaySend(QueueConstants.QING_SHI_ORDER_PAY_NAME, order.getId().toString(), 15 * 60 * 1000L);

        CreateOrderVo createOrderVo = new CreateOrderVo();
        createOrderVo.setOrderId(order.getId());
        createOrderVo.setOrderNo(order.getOrderNo());
        createOrderVo.setOrderPrice(orderPrice);
        //返回订单过期时间
        createOrderVo.setExpireTime(getExpireTime(order.getCreateTime()));

        return createOrderVo;
    }


    private void checkShop(Shop shop) {
        //判断店铺是否营业
        if (!shopService.checkShopStatus(shop)) {
            throw new PinetException("店铺已经打烊了~");
        }

    }


    /**
     * 设置佣金
     *
     * @param orders
     */
    private void setOrdersCommission(Orders orders, List<OrderProduct> orderProducts) {
        if (ObjectUtil.isNull(orders.getShareId()) || orders.getShareId() <= 0) {
            return;
        }

        //判断下单人和分享人是否是同一个人
        if (orders.getCustomerId().equals(orders.getShareId())) {
            return;
        }

        //下单人会员等级
        Integer customerMemberLevel = customerMemberService.getMemberLevel(orders.getCustomerId());
        //分享人会员等级
        Integer shareMemberLevel = customerMemberService.getMemberLevel(orders.getShareId());

        //邀请人必须是店帮主  被邀人不能是店帮主
        if (shareMemberLevel.equals(MemberLevelEnum._20.getCode()) && !customerMemberLevel.equals(MemberLevelEnum._20.getCode())) {
            //佣金=商品总金额 * 0.1
            BigDecimal commission = orders.getOrderProdPrice().multiply(new BigDecimal("0.1")).setScale(2, RoundingMode.HALF_UP);
            orders.setCommission(commission);


            //设置单个商品的佣金
            orderProducts.forEach(k -> {
                k.setCommission(k.getProdPrice().multiply(new BigDecimal("0.1")).setScale(2, RoundingMode.HALF_UP));
            });

        }
    }


    /**
     * 处理优惠券
     *
     * @param customerCouponId 使用的优惠券id
     * @param shopId           店铺id
     * @param orderProdPrice   实付金额
     * @param type             1结算  2下单
     * @return 使用完优惠券后的价格
     */
    private BigDecimal processCoupon(Long customerCouponId, Long shopId, BigDecimal orderProdPrice,
                                     List<OrderDiscount> orderDiscounts, Integer type) {
        CustomerCoupon customerCoupon = customerCouponService.getById(customerCouponId);
        Boolean checkFlag = customerCouponService.checkCoupon(customerCoupon, shopId, orderProdPrice);
        if (!checkFlag) {
            throw new PinetException("优惠券不可用");
        }

        //添加优惠明细信息
        OrderDiscount orderDiscount = new OrderDiscount();
        orderDiscount.setDiscountMsg("满减优惠券").setType(2).setDiscountAmount(customerCoupon.getCouponAmount());
        orderDiscounts.add(orderDiscount);

        //更新优惠券为已使用
        if (type == 2) {
            customerCoupon.setCouponStatus(4);
            customerCouponService.updateById(customerCoupon);
        }
        return orderProdPrice.subtract(customerCoupon.getCouponAmount());
    }


    private Long getExpireTime(Date createTime) {
        Date expireTime = DateUtil.offsetMinute(createTime, 15);
        return expireTime.getTime();
    }

    @Override
    @DSTransactional
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

        //封装PayParam
        PayParam param = new PayParam();
        param.setOpenId(dto.getOpenId());
        param.setOrderNo(orders.getOrderNo().toString());
        param.setPayPrice(dto.getOrderPrice());
        param.setPayDesc("轻食订单下单");
        param.setPayType(1);
        param.setPayPassWord(dto.getPayPassword());
        param.setOrderId(dto.getOrderId());
        Object res = payService.pay(param);
        return res;
    }

    @Override
    @DSTransactional
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

        //商家该订单收益= 用户支付总金额  - 配送费
        BigDecimal shopEarnings = orderPay.getPayPrice().subtract(orders.getShippingFee());


        //修改余额
        ibUserBalanceService.addAmount(orders.getShopId(), shopEarnings);

        //资金流水
        bCapitalFlowService.add(shopEarnings, orders.getId(), orders.getCreateTime(),
                CapitalFlowWayEnum.getEnumByChannelId(orderPay.getChannelId()), CapitalFlowStatusEnum._1, orders.getShopId());

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
            RefundParam refundParam = new RefundParam(orders.getOrderPrice().toString(),
                    orders.getOrderNo().toString(),
                    orderRefund.getRefundNo().toString(),
                    orders.getOrderPrice().toString(),
                    orderRefund.getId(), orders.getCustomerId());
            payService.refund(refundParam);
            orders.setOrderStatus(OrderStatusEnum.REFUND.getCode());
            return updateById(orders);
        } else {
            //推送客如云,订单状态  1外卖  2自提
            if (orders.getOrderType() == 1) {
                orders.setOrderStatus(OrderStatusEnum.SEND_OUT.getCode());
                String kryOrderNo = takeoutOrderCreate(orders);
                orders.setKryOrderNo(kryOrderNo);
                orders.setOrderStatus(OrderStatusEnum.PAY_COMPLETE.getCode());
                //创建配送订单
                try {
                    daDaService.createOrder(orders);
                } catch (RpcException e) {
                    throw new PinetException("外卖订单创建异常，请联系商家处理");
                }
            } else {
                //自提单
                String kryOrderNo = scanCodePrePlaceOrder(orders);
                orders.setKryOrderNo(kryOrderNo);
                orders.setOrderStatus(OrderStatusEnum.COMPLETE.getCode());
                //判断订单是否有佣金 如果有佣金 && 订单状态是已完成 设置佣金三天后到账
                if (orders.getCommission().compareTo(BigDecimal.ZERO) > 0) {
                    jmsUtil.delaySend(QueueConstants.QING_SHI_ORDER_COMMISSION, orders.getId().toString(), 3 * 24 * 60 * 60 * 1000L);
                }

                //自提订单发送短信
                //先用mq异步发送  (后期可能会删除)
                jmsUtil.delaySend(QueueConstants.QING_ORDER_SEND_SMS_NAME, JSONObject.toJSONString(orders), 10 * 1000L);

            }
            return updateById(orders);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        boolean res = updateById(orders);

        //判断是否使用了优惠券
        if (orders.getCustomerCouponId() != null && orders.getCustomerCouponId() > 0) {
            CustomerCoupon customerCoupon = customerCouponService.getById(orders.getCustomerCouponId());
            customerCoupon.setCouponStatus(2);
            customerCouponService.updateById(customerCoupon);
        }

        return res;
    }

    @Override
    @DSTransactional
    public Boolean orderRefundNotify(OrderRefundNotifyParam param) {

        LambdaQueryWrapper<OrderRefund> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderRefund::getRefundNo, param.getRefundNo()).eq(BaseEntity::getDelFlag, 0);
        OrderRefund orderRefund = orderRefundService.getOne(lambdaQueryWrapper);
        if (orderRefund == null) {
            log.error("微信退款回调失败，退款单号不存在" + param.getRefundNo());
            throw new PinetException("退款单号不存在");
        }
        //订单信息
        Orders orders = getById(orderRefund.getOrderId());
        //订单支付信息
        OrderPay orderPay = orderPayService.getById(orderRefund.getOrderPayId());

        //商家该订单收益= 用户支付总金额  - 配送费
        BigDecimal shopEarnings = orderPay.getPayPrice().subtract(orders.getShippingFee());


        //暂时退款配送费由商家承担
        //资金流水
        bCapitalFlowService.add(orderPay.getPayPrice().negate(), orders.getId(), orders.getCreateTime(),
                CapitalFlowWayEnum.getEnumByChannelId(orderPay.getChannelId()), CapitalFlowStatusEnum._2, orders.getShopId());

        //修改余额
        ibUserBalanceService.addAmount(orders.getShopId(), orderPay.getPayPrice().negate());


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
    public BigDecimal getDiscountedPrice(Long customerId, BigDecimal originalPrice, List<OrderDiscount> orderDiscounts) {
        Integer memberLevel = customerMemberService.getMemberLevel(customerId);
        MemberLevelEnum memberLevelEnum = MemberLevelEnum.getEnumByCode(memberLevel);

        BigDecimal discountedPrice = originalPrice.multiply(memberLevelEnum.getDiscount()).setScale(2, RoundingMode.HALF_UP);

        //如果不是门客  添加优惠明细信息
        if (!memberLevel.equals(MemberLevelEnum._0.getCode())) {
            OrderDiscount orderDiscount = new OrderDiscount();
            orderDiscount.setDiscountMsg(memberLevelEnum.getMsg() + memberLevelEnum.getDiscount().multiply(new BigDecimal(10)) + "优惠")
                    .setDiscountAmount(originalPrice.subtract(discountedPrice)).setType(1);
            orderDiscounts.add(orderDiscount);
        }
        return discountedPrice;
    }

    @Override
    public List<PickUpListVo> pickUpList() {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        List<PickUpListVo> pickUpListVos = baseMapper.selectPickUpList(customerId);
        pickUpListVos.forEach(k -> {
            if (k.getOrderStatus().equals(OrderStatusEnum.MAKE.getCode())) {
                k.setOrderStatusStr(OrderStatusEnum.MAKE.getMsg());
            } else {
                k.setOrderStatusStr("可领取");
            }
        });
        return pickUpListVos;
    }


    private Orders createOrder(CreateOrderDto dto, BigDecimal shippingFee, Double m, BigDecimal orderPrice, BigDecimal orderProdPrice, BigDecimal discountAmount, Shop shop, BigDecimal packageFee) {
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
        order.setPackageFee(packageFee);
        order.setEstimateArrivalStartTime(estimateArrivalStartTime);
        order.setEstimateArrivalEndTime(estimateArrivalEndTime);
        order.setOrderDistance(m.intValue());
        order.setRemark(dto.getRemark());
        order.setShareId(dto.getShareId());
        order.setKryShopId(shop.getKryShopId());
        order.setOrderSource(dto.getOrderSource());
        return order;
    }


    /**
     * 获取配送费
     *
     * @param orderType    订单类型( 1外卖  2自提)
     * @param selfDelivery 是否商家自配送( 0-否，1-是)
     * @param distance 距离 单位m
     * @return BigDecimal
     */
    private BigDecimal getShippingFee(Integer orderType, Long customerAddressId, BigDecimal orderProdPrice, String deliveryShopNo, Integer selfDelivery,Double distance) {
        if (orderType == 2) {
            return new BigDecimal("0");
        }
        //测试环境默认4元吧
        if (!"prod".equals(active)) {
            return new BigDecimal("4");
        }
        if (StringUtil.isBlank(deliveryShopNo) || selfDelivery == 1) {
            //todo 商家没有对接外卖平台，自配送
            return new BigDecimal("5");
        }
        //查询收货地址
        CustomerAddress customerAddress = customerAddressService.getById(customerAddressId);
        if (ObjectUtil.isNull(customerAddress)) {
            throw new PinetException("收货地址异常");
        }

        Snowflake snowflake = IdUtil.getSnowflake();

        AddOrderReq addOrderReq = AddOrderReq.builder()
                .shopNo(deliveryShopNo)
                .originId(snowflake.nextIdStr())
                .cargoPrice(orderProdPrice.doubleValue())
                .prepay(0)
                .receiverName(customerAddress.getName())
                .receiverAddress(customerAddress.getAddress())
                .receiverPhone(customerAddress.getPhone())
                .callback("http://xinjiangapi.ypxlbz.com/house/qingshi/api/dada/deliverFee/callback")
                .cargoWeight(0.5)
                .receiverLat(customerAddress.getLat().doubleValue())
                .receiverLng(customerAddress.getLng().doubleValue())
                .build();
        try {
            AddOrderResp addOrderResp = daDaService.queryDeliverFee(addOrderReq);
            return BigDecimal.valueOf(addOrderResp.getDeliverFee());
        } catch (RpcException e) {
            throw new PinetException("查询配送费服务失败");
        }

//        if(distance <= 1000d){
//            return new BigDecimal("4");
//        }else if(distance > 1000 && distance <= 2000){
//            return new BigDecimal("4.5");
//        }else if(distance > 2000 && distance <= 3000){
//            return new BigDecimal("5");
//        }else if(distance > 3000 && distance <= 4000){
//            return new BigDecimal("6.5");
//        }else {
//            throw new PinetException("距离超过配送范围");
//        }
    }



    /**
     * 分割商品样式id
     */
    private List<Long> splitShopProdSpecIds(String shopProdSpecIds) {
        String[] idArray = shopProdSpecIds.split(",");

        return Arrays.stream(idArray)
                .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    @DSTransactional
    public boolean syncOrderStatus(OrderSyncDTO dto) {
        //退款
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("kry_order_no", dto.getOrderBody().getRelatedOrderNo());
        Orders orders = getOne(queryWrapper);

        //校验:
        //查看是否已经退款
        if (orders.getOrderStatus().equals(OrderStatusEnum.REFUND.getCode())) {
            throw new PinetException("订单已退款");
        }
        //判断订单状态
        OrderPay orderPay = orderPayService.getOne(Wrappers.lambdaQuery(new OrderPay())
                .eq(OrderPay::getOrderId, orders.getId())
                .eq(OrderPay::getPayStatus, 2)
        );
        if (orderPay == null) {
            throw new PinetException("订单支付信息异常");
        }

        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setRefundNo(IdUtil.getSnowflake().nextId());
        orderRefund.setOrderId(orders.getId());
        orderRefund.setOrderPayId(orderPay.getId());
        orderRefund.setRefundPrice(orders.getOrderPrice());
        orderRefund.setOrderPrice(orders.getOrderPrice());
        orderRefund.setIsAllRefund(true);
        orderRefund.setRefundDesc("运营后台操作退款");
        orderRefund.setRefundStatus(1);
        orderRefundService.save(orderRefund);
        //调退款
        IPayService payService = SpringContextUtils.getBean(orderPay.getChannelId() + "_" + "service", IPayService.class);
        RefundParam refundParam = new RefundParam(
                orders.getOrderPrice().toString(),
                orders.getOrderNo().toString(),
                orderRefund.getRefundNo().toString(),
                orders.getOrderPrice().toString(),
                orderRefund.getId(), orders.getCustomerId());
        payService.refund(refundParam);
        //更新订单状态
        orders.setOrderStatus(OrderStatusEnum.REFUND.getCode());
        updateById(orders);


        //取消配送
        if (orders.getOrderType() == 1) {
            daDaService.cancelOrder(orders.getOrderNo());
        }
        return true;
    }

    @Override
    public boolean completeOrder(Long orderId) {
        Orders orders = getById(orderId);
        orders.setOrderStatus(OrderStatusEnum.COMPLETE.getCode());
        updateById(orders);

        //判断订单是否有佣金 如果有佣金 && 订单状态是已完成 设置佣金三天后到账
        if (orders.getCommission().compareTo(BigDecimal.ZERO) > 0) {
            jmsUtil.delaySend(QueueConstants.QING_SHI_ORDER_COMMISSION, orders.getId().toString(), (long) (15 * 60 * 1000));
        }
        return true;
    }


    /**
     * https://open.keruyun.com/docs/zh/UnJOKokB77V9K553GtMO.html
     * 客如云外卖下单
     *
     * @param order
     * @return
     */
    public String takeoutOrderCreate(Orders order) {
        if (!Objects.equals(active, "prod")) {
            return "";
        }
        KryOpenTakeoutOrderCreateDTO takeoutOrderCreateDTO = new KryOpenTakeoutOrderCreateDTO();
        takeoutOrderCreateDTO.setOutBizNo(String.valueOf(order.getOrderNo()));
        takeoutOrderCreateDTO.setRemark(order.getRemark());
        takeoutOrderCreateDTO.setOrderSecondSource("WECHAT_MINI_PROGRAM");
        takeoutOrderCreateDTO.setPromoFee(BigDecimalUtil.yuanToFen(order.getDiscountAmount()));//优惠
        takeoutOrderCreateDTO.setActualFee(BigDecimalUtil.yuan2Fen(order.getOrderPrice()));//应付
        takeoutOrderCreateDTO.setTotalFee(BigDecimalUtil.yuanToFen(BigDecimalUtil.sum(order.getOrderPrice(), order.getDiscountAmount())));

        PromoDetailRequest promoDetailRequest = new PromoDetailRequest();
        promoDetailRequest.setOutPromoDetailId(UUID.randomUUID().toString());
        promoDetailRequest.setPromoId(UUID.randomUUID().toString());
        promoDetailRequest.setPromoName("优惠");
        promoDetailRequest.setPromoFee(BigDecimalUtil.yuan2Fen(order.getDiscountAmount()));
        promoDetailRequest.setPromoCategory("ORDER_DIMENSION");
        promoDetailRequest.setPromoDiscount(null);
        promoDetailRequest.setPromoType("THIRD_MERCHANT");
        promoDetailRequest.setPromoDimension("TOATL_CART");
        takeoutOrderCreateDTO.setPromoDetailRequestList(Arrays.asList(promoDetailRequest));

        DcOrderBizRequest dcOrderBizRequest = new DcOrderBizRequest();
        dcOrderBizRequest.setDinnerType("DELIVERY");//外送
        dcOrderBizRequest.setTakeoutFee(BigDecimalUtil.yuan2Fen(order.getPackageFee()));
        dcOrderBizRequest.setTableWareFee(0L);
        dcOrderBizRequest.setTakeMealType("SELF_TAKE");
        takeoutOrderCreateDTO.setDcOrderBizRequest(dcOrderBizRequest);

        OrderStrategyRequest orderStrategyRequest = new OrderStrategyRequest();
        orderStrategyRequest.setValidateDishStock(true);
        takeoutOrderCreateDTO.setOrderStrategyRequest(orderStrategyRequest);

        OrderPay orderPay = orderPayService.getByOrderNo(order.getOrderNo());
        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest();
        paymentDetailRequest.setOutBizId(String.valueOf(orderPay.getId()));
        paymentDetailRequest.setAmount(BigDecimalUtil.yuanToFen(orderPay.getPayPrice()));
        paymentDetailRequest.setChangeAmount(0);
        paymentDetailRequest.setPayMode("KEEP_ACCOUNT");
        paymentDetailRequest.setChannelCode("OPENTRADE_WECHAT_PAY");
        paymentDetailRequest.setPayType("PAY_MODE_SMALL_PROGRAM");
        takeoutOrderCreateDTO.setPaymentDetailRequestList(Arrays.asList(paymentDetailRequest));

        //订单附加费列表,可不填
        ExtraFeeRequest extraFeeRequest = new ExtraFeeRequest();
        extraFeeRequest.setOutExtraFeeDetailNo(UUID.randomUUID().toString());
        extraFeeRequest.setExtraFeeType("DELIVERY_FEE");
        extraFeeRequest.setCustomExtraFeeName("配送费");
        extraFeeRequest.setExtraTotalFee(BigDecimalUtil.yuan2Fen(order.getShippingFee()));
        extraFeeRequest.setExtraActualFee(BigDecimalUtil.yuan2Fen(order.getShippingFee()));
        extraFeeRequest.setExtraPromoFee(0L);
        extraFeeRequest.setExtraCalType("CALCULATE_BY_FIXED");
        extraFeeRequest.setParticipateDiscountFlag(false);
        extraFeeRequest.setParticipateSplitFlag(false);
        extraFeeRequest.setExtraRuleId("");
        //extraFeeRequest.setExtraCalValue("");
        takeoutOrderCreateDTO.setExtraFeeRequestList(Arrays.asList(extraFeeRequest));

        List<OrderProductDto> orderProducts = orderProductService.selectByOrderId(order.getId());
        List<OrderDishRequest> orderDishRequestList = new ArrayList<>(orderProducts.size());
        for (OrderProductDto orderProduct : orderProducts) {
            OrderDishRequest orderDishRequest = new OrderDishRequest();
            orderDishRequest.setOutDishNo(String.valueOf(orderProduct.getId()));
            orderDishRequest.setDishId(orderProduct.getProdId());
            orderDishRequest.setDishCode(orderProduct.getDishCode());
            orderDishRequest.setDishName(orderProduct.getProductName());
            orderDishRequest.setDishQuantity(orderProduct.getProdNum());
            orderDishRequest.setDishFee(BigDecimalUtil.yuanToFen(orderProduct.getProdUnitPrice()));
            orderDishRequest.setDishOriginalFee(BigDecimalUtil.yuanToFen(orderProduct.getProdUnitPrice()));
            orderDishRequest.setTotalFee(BigDecimalUtil.yuanToFen(BigDecimalUtil.sum(orderProduct.getProdPrice(), orderProduct.getPackageFee())));
            orderDishRequest.setPromoFee(0L);
            orderDishRequest.setActualFee(BigDecimalUtil.yuanToFen(orderProduct.getProdPrice()));
            orderDishRequest.setUnitId(orderProduct.getUnitId());
            orderDishRequest.setUnitName(orderProduct.getUnit());
            orderDishRequest.setUnitCode(orderProduct.getUnitId());
            //附加项（加料、做法）列表

            orderDishRequest.setDishSkuId(orderProduct.getKrySkuId());
            orderDishRequest.setWeightDishFlag(false);
            orderDishRequest.setDishImgUrl(orderProduct.getProductImg());
            orderDishRequest.setIsPack(true);
            orderDishRequest.setPackageFee(BigDecimalUtil.yuan2FenStr(orderProduct.getPackageFee()));
            List<ScanCodeDish> dishList = new ArrayList<>();
            if ("SINGLE".equalsIgnoreCase(orderProduct.getDishType())) {
                orderDishRequest.setItemOriginType("SINGLE");
                orderDishRequest.setDishType("SINGLE_DISH");
                orderDishRequest.setDishAttachPropList(getDishAttachPropList(orderProduct.getOrderProductId()));
            } else if ("COMBO".equalsIgnoreCase(orderProduct.getDishType())) {
                orderDishRequest.setItemOriginType("COMBO");
                orderDishRequest.setDishType("COMBO_DISH");
                List<KryComboGroupDetailVo> kryComboGroupDetailList = kryComboGroupDetailService.getByOrderProdId(orderProduct.getOrderProductId(),order.getShopId());
                for (KryComboGroupDetailVo groupDetail : kryComboGroupDetailList) {
                    ScanCodeDish dish = new ScanCodeDish();
                    dish.setOutDishNo(String.valueOf(groupDetail.getId()));
                    dish.setDishId(groupDetail.getSingleDishId());
                    dish.setDishType("COMBO_DETAIL");
                    dish.setDishCode(groupDetail.getDishCode());
                    dish.setDishName(groupDetail.getDishName());
                    dish.setDishQuantity(new BigDecimal(String.valueOf(orderProduct.getProdNum())));
                    dish.setDishFee(BigDecimalUtil.yuan2Fen(orderProduct.getProdUnitPrice()));
                    dish.setUnitId(groupDetail.getUnitId());
                    dish.setUnitCode(groupDetail.getUnit());
                    dish.setUnitName(groupDetail.getUnit());
                    dish.setDishOriginalFee(groupDetail.getSellPrice());
                    dish.setTotalFee(groupDetail.getSellPrice());
                    dish.setPromoFee(0L);
                    dish.setActualFee(groupDetail.getSellPrice());
                    dish.setPackageFee("0");
                    dish.setWeightDishFlag("0");
                    dish.setDishImgUrl(groupDetail.getImageUrl());
                    dish.setIsPack("false");
                    dish.setDishGiftFlag("false");
                    dish.setItemOriginType("SINGLE");
                    dish.setDishSkuId(orderProduct.getKrySkuId());
                    dishList.add(dish);
                }
            }
            if(!CollectionUtils.isEmpty(dishList)){
                orderDishRequest.setDishList(dishList);
            }
            orderDishRequestList.add(orderDishRequest);
        }

        takeoutOrderCreateDTO.setOrderDishRequestList(orderDishRequestList);

        OrderAddress orderAddress = orderAddressService.getOrderAddress(order.getId());
        DeliveryInfoRequest deliveryInfoRequest = new DeliveryInfoRequest();
        deliveryInfoRequest.setDeliveryType("RECIPIENT_ADDRESS");
        deliveryInfoRequest.setOutBizId(String.valueOf(order.getOrderNo()));
        deliveryInfoRequest.setReceiverAddress(orderAddress.getAddress());
        deliveryInfoRequest.setReceiverGender(orderAddress.getSex() == null ? null : orderAddress.getSex() == 1 ? "MAN" : "WOMAN");
        deliveryInfoRequest.setReceiverPrimaryPhone(orderAddress.getTel());
        deliveryInfoRequest.setReceiverName(orderAddress.getName());
        deliveryInfoRequest.setMapType(2);
        deliveryInfoRequest.setLatitude(new BigDecimal(orderAddress.getLat()));
        deliveryInfoRequest.setLongitude(new BigDecimal(orderAddress.getLng()));
        takeoutOrderCreateDTO.setDeliveryInfoRequestList(Arrays.asList(deliveryInfoRequest));
        String token = kryApiService.getToken(AuthType.SHOP, order.getKryShopId());
        TakeoutOrderCreateVo takeoutOrderCreateVo = kryApiService.openTakeoutOrderCreate(order.getKryShopId(), token, takeoutOrderCreateDTO);
        //记录日志
        pushKryOrderLog(order.getId(), JSONObject.toJSONString(takeoutOrderCreateDTO), JSONObject.toJSONString(takeoutOrderCreateVo), takeoutOrderCreateVo.getSuccess());

        if (takeoutOrderCreateVo == null) {
            return null;
        }
        if ("false".equals(takeoutOrderCreateVo.getSuccess())) {
            //推送失败，重试
            pushKryOrderMessage(order, takeoutOrderCreateVo.getFormatMsgInfo());
            return null;
        }
        if (takeoutOrderCreateVo.getData() == null) {
            return null;
        }
        return takeoutOrderCreateVo.getData().getOrderNo();
    }

    /**
     * 参考文档： https://open.keruyun.com/docs/zh/p3JOKokB77V9K553kNMI.html
     * 堂食扫码下单
     *
     * @param orders
     * @return
     */
    @Override
    public String scanCodePrePlaceOrder(Orders orders) {
        //生产环境才推单，其他环境就不推了吧
        if (!"prod".equals(active)) {
            return null;
        }
        KryScanCodeOrderCreateDTO dto = new KryScanCodeOrderCreateDTO();
        dto.setOutBizNo(String.valueOf(orders.getOrderNo()));
        dto.setRemark(orders.getRemark());
        dto.setOrderSecondSource("WECHAT_MINI_PROGRAM");
        dto.setPromoFee(BigDecimalUtil.yuanToFen(orders.getDiscountAmount()));
        dto.setActualFee(BigDecimalUtil.yuanToFen(orders.getOrderPrice()));
        dto.setTotalFee(BigDecimalUtil.yuanToFen(BigDecimalUtil.sum(orders.getOrderPrice(), orders.getDiscountAmount())));

        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest();
        paymentDetailRequest.setOutBizId(String.valueOf(orders.getId()));
        paymentDetailRequest.setAmount(BigDecimalUtil.yuanToFen(orders.getOrderPrice()));
        paymentDetailRequest.setPayMode("KEEP_ACCOUNT");
        paymentDetailRequest.setChannelCode("OPENTRADE_WECHAT_PAY");
        paymentDetailRequest.setPayType("PAY_MODE_SMALL_PROGRAM");
        dto.setPaymentDetailRequestList(Arrays.asList(paymentDetailRequest));

        OrderStrategyRequest orderStrategyRequest = new OrderStrategyRequest();
        orderStrategyRequest.setValidateDishStock(false);
        dto.setOrderStrategyRequest(orderStrategyRequest);

        DcOrderBizRequest dcOrderBizRequest = new DcOrderBizRequest();
        dcOrderBizRequest.setTakeMealType("SELF_TAKE");
        dcOrderBizRequest.setTableWareFee(0L);
        dcOrderBizRequest.setDinnerType("TAKE_OUT");
        dcOrderBizRequest.setTakeoutFee(0L);
        dto.setDcOrderBizRequest(dcOrderBizRequest);

        PromoDetailRequest promoDetailRequest = new PromoDetailRequest();
        promoDetailRequest.setOutPromoDetailId(UUID.randomUUID().toString());
        promoDetailRequest.setPromoId(UUID.randomUUID().toString());
        promoDetailRequest.setPromoName("优惠");
        promoDetailRequest.setPromoFee(BigDecimalUtil.yuan2Fen(orders.getDiscountAmount()));
        promoDetailRequest.setPromoCategory("ORDER_DIMENSION");
        promoDetailRequest.setPromoDiscount(null);
        promoDetailRequest.setPromoType("THIRD_MERCHANT");
        promoDetailRequest.setPromoDimension("TOATL_CART");
        dto.setPromoDetailRequestList(Arrays.asList(promoDetailRequest));

        List<OrderProductDto> orderProducts = orderProductService.selectByOrderId(orders.getId());
        List<OrderDishRequest> orderDishRequestList = new ArrayList<>();
        for (OrderProductDto orderProduct : orderProducts) {
            OrderDishRequest request = new OrderDishRequest();
            request.setOutDishNo(String.valueOf(orderProduct.getId()));
            request.setDishId(orderProduct.getProdId());
            request.setDishName(orderProduct.getProductName());
            request.setDishCode(orderProduct.getDishCode());
            request.setDishQuantity(orderProduct.getProdNum());
            request.setDishFee(BigDecimalUtil.yuanToFen(orderProduct.getProdUnitPrice()));
            request.setDishOriginalFee(BigDecimalUtil.yuanToFen(orderProduct.getProdUnitPrice()));
            request.setTotalFee(BigDecimalUtil.yuanToFen(orderProduct.getProdPrice()));
            request.setPromoFee(0L);
            request.setActualFee(request.getTotalFee().intValue() - request.getPromoFee().intValue());
            request.setPackageFee("0");
            request.setDishSkuId(orderProduct.getKrySkuId());
            request.setDishSkuCode(orderProduct.getSkuCode());
            request.setDishSkuName(orderProduct.getSkuName());
            request.setWeightDishFlag(false);
            request.setUnitCode(orderProduct.getUnitId());
            request.setUnitId(orderProduct.getUnitId());
            request.setUnitName(orderProduct.getUnit());

            List<ScanCodeDish> dishList = new ArrayList<>();
            //配料明细 或者 套餐明细
            if ("SINGLE".equalsIgnoreCase(orderProduct.getDishType())) {
                //配料明细
                //附加项（加料、做法）列表
                request.setDishType("SINGLE_DISH");
                request.setItemOriginType("SINGLE");
                request.setDishAttachPropList(getDishAttachPropList(orderProduct.getOrderProductId()));
            } else if ("COMBO".equalsIgnoreCase(orderProduct.getDishType())) {
                request.setDishType("COMBO_DISH");
                request.setItemOriginType("COMBO");
                //套餐明细
                List<KryComboGroupDetailVo> kryComboGroupDetailList = kryComboGroupDetailService.getByOrderProdId(orderProduct.getOrderProductId(),orders.getShopId());
                for (KryComboGroupDetailVo groupDetail : kryComboGroupDetailList) {
                    ScanCodeDish dish = new ScanCodeDish();
                    dish.setOutDishNo(String.valueOf(groupDetail.getId()));
                    dish.setOutDishNo(UUID.randomUUID().toString());
                    dish.setDishId(groupDetail.getSingleDishId());
                    dish.setDishType("COMBO_DETAIL");
                    dish.setDishCode(groupDetail.getDishCode());
                    dish.setDishName(groupDetail.getDishName());
                    dish.setDishQuantity(new BigDecimal(String.valueOf(orderProduct.getProdNum())));
                    dish.setDishFee(BigDecimalUtil.yuan2Fen(orderProduct.getProdUnitPrice()));
                    dish.setUnitId(groupDetail.getUnitId());
                    dish.setUnitCode(groupDetail.getUnit());
                    dish.setUnitName(groupDetail.getUnit());
                    dish.setDishOriginalFee(groupDetail.getSellPrice());
                    dish.setTotalFee(groupDetail.getSellPrice());
                    dish.setPromoFee(0L);
                    dish.setActualFee(groupDetail.getSellPrice());
                    dish.setPackageFee("0");
                    dish.setWeightDishFlag("0");
                    dish.setDishImgUrl(groupDetail.getImageUrl());
                    dish.setIsPack("false");
                    dish.setDishGiftFlag("false");
                    dish.setItemOriginType("SINGLE");
                    dish.setDishSkuId(orderProduct.getKrySkuId());
                    dishList.add(dish);
                }
            }
            if (!CollectionUtils.isEmpty(dishList)) {
                request.setDishList(dishList);
            }
            request.setIsPack(false);
            orderDishRequestList.add(request);
        }
        dto.setOrderDishRequestList(orderDishRequestList);
        String token = kryApiService.getToken(AuthType.SHOP, orders.getKryShopId());
        ScanCodePrePlaceOrderVo scanCodePrePlaceOrderVo = kryApiService.scanCodePrePlaceOrder(orders.getKryShopId(), token, dto);
        //记录日志
        pushKryOrderLog(orders.getId(), JSONObject.toJSONString(dto), JSONObject.toJSONString(scanCodePrePlaceOrderVo), scanCodePrePlaceOrderVo.getSuccess());

        if (scanCodePrePlaceOrderVo == null) {
            return null;
        }
        if ("fail".equals(scanCodePrePlaceOrderVo.getSuccess())) {
            //推送失败，重试
            pushKryOrderMessage(orders, scanCodePrePlaceOrderVo.getFormatMsgInfo());
            return null;
        }
        if (scanCodePrePlaceOrderVo.getData() == null) {
            return null;
        }
        return scanCodePrePlaceOrderVo.getData().getOrderNo();
    }


    /**
     * 获取订单商品做法数据
     *
     * @param orderProdId
     * @return
     */
    private List<DishAttachProp> getDishAttachPropList(Long orderProdId) {
        List<OrderProductSpec> orderProductSpecs = orderProductSpecService.getByOrderProdId(orderProdId);
        List<DishAttachProp> dishAttachPropList = new ArrayList<>();
        for (OrderProductSpec spec : orderProductSpecs) {
            if ("标准".equals(spec.getProdSpecName()) && "规格".equals(spec.getProdSkuName())) {
                //这个不是做法
                continue;
            }
            String id = String.valueOf(spec.getId());
            DishAttachProp dishAttachProp = new DishAttachProp();
            dishAttachProp.setOutAttachPropNo(id);
            dishAttachProp.setAttachPropType("PRACTICE");
            dishAttachProp.setAttachPropCode(id);
            dishAttachProp.setAttachPropName(spec.getProdSpecName());
            dishAttachProp.setPrice(0L);
            dishAttachProp.setQuantity(1);
            dishAttachProp.setTotalFee(0L);
            dishAttachProp.setPromoFee(0L);
            dishAttachProp.setActualFee(0L);
            dishAttachProp.setAttachPropId(id);
            dishAttachPropList.add(dishAttachProp);
        }
        return CollectionUtils.isEmpty(dishAttachPropList) ? null : dishAttachPropList;
    }


    /**
     * 推送客如云订单的消息
     *
     * @param orders
     * @param message 失败原因
     */
    private void pushKryOrderMessage(Orders orders, String message) {
        KryOrderCompensate kryOrderCompensate = new KryOrderCompensate();
        kryOrderCompensate.setKryOrderNo(orders.getKryOrderNo());
        kryOrderCompensate.setOrderId(orders.getId());
        kryOrderCompensate.setTimes(0);
        kryOrderCompensate.setStatus(0);
        kryOrderCompensate.setPushTime(new Date());
        kryOrderCompensate.setFailReason(message);
        kryOrderCompensateService.save(kryOrderCompensate);
    }

    /**
     * 客如云订单日志
     *
     * @param orderId
     * @param param
     * @param res
     */
    private void pushKryOrderLog(Long orderId, String param, String res, String success) {
        KryOrderPushLog log = new KryOrderPushLog();
        log.setOrderId(orderId);
        log.setParams(param);
        log.setPushTime(new Date());
        log.setPushRes(res);
        log.setStatus("fail".equals(success) ? 0 : 1);
        kryOrderPushLogService.save(log);
    }
}
