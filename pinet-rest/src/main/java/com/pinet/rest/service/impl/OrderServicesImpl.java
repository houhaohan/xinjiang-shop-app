package com.pinet.rest.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
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
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.DB;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.exception.PinetException;
import com.pinet.core.page.PageRequest;
import com.pinet.core.util.*;
import com.pinet.keruyun.openapi.dto.*;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.keruyun.openapi.service.IKryApiService;
import com.pinet.keruyun.openapi.type.AuthType;
import com.pinet.keruyun.openapi.vo.OrderDetailVO;
import com.pinet.keruyun.openapi.vo.ScanCodePrePlaceOrderVo;
import com.pinet.keruyun.openapi.vo.TakeoutOrderCreateVo;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.*;
import com.pinet.rest.entity.enums.*;
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
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Resource
    private IShippingFeeRuleService shippingFeeRuleService;

    @Resource
    private IShopProductService shopProductService;

    @Resource
    private IScoreRecordService scoreRecordService;

    @Resource
    private ICustomerBalanceService customerBalanceService;

    @Autowired
    private OrderPreferentialManager orderPreferentialManager;

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

        if (StringUtil.isBlank(orderDetailVo.getMealCode()) && Environment.isProd()) {
            String token = kryApiService.getToken(AuthType.SHOP, orderDetailVo.getKryShopId());
            KryOrderDetailDTO kryOrderDetailDTO = new KryOrderDetailDTO();
            kryOrderDetailDTO.setOrderId(orderDetailVo.getKryOrderNo());
            OrderDetailVO orderDetail = kryApiService.getOrderDetail(orderDetailVo.getKryShopId(), token, kryOrderDetailDTO);
            if (orderDetail != null) {
                orderDetailVo.setMealCode(orderDetail.getOrderBaseVO().getSerialNo());
            }
        }
        return orderDetailVo;
    }

    @Override
    public OrderSettlementVo orderSettlement(OrderSettlementDto dto) {

        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();

        Shop shop = shopService.getById(dto.getShopId());
        //判断店铺是否营业
        checkShop(shop);
        if (dto.getOrderType() == 1 && shop.getSupportDelivery() == 0) {
            throw new PinetException("该门店暂不支持外卖");
        }

        double distance = getDistance(dto.getCustomerAddressId(), dto.getOrderType(), shop);

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
        vo.setOrderMakeCount(countShopOrderMakeNum(dto.getShopId()));
        //计算商品总金额
        BigDecimal orderProdPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //配送费
        BigDecimal shippingFee = getShippingFee(dto.getOrderType(), distance, shop.getDeliveryPlatform());
        vo.setShippingFee(shippingFee);

        //设置订单原价 和 商品原价
        vo.setOriginalPrice(BigDecimalUtil.sum(orderProdPrice, shippingFee, packageFee));
        vo.setOriginalOrderProductPrice(orderProdPrice);

        //订单优惠处理
        PreferentialVo preferentialVo = orderPreferentialManager.doPreferential(customerId, dto.getCustomerCouponId(), orderProdPrice, orderProducts);
        vo.setOrderPrice(BigDecimalUtil.sum(preferentialVo.getProductDiscountAmount(),packageFee,shippingFee));

        //返回预计送达时间
        Date now = new Date();
        String estimateArrivalStartTime = DateUtil.format(DateUtil.offsetMinute(now, 15), "HH:mm");
        String estimateArrivalEndTime = DateUtil.format(DateUtil.offsetMinute(now, 45), "HH:mm");

        vo.setEstimateArrivalTime(estimateArrivalStartTime + "-" + estimateArrivalEndTime);


        Integer orderProductNum = orderProducts.stream().map(OrderProduct::getProdNum).reduce(Integer::sum).orElse(0);
        vo.setOrderProductNum(orderProductNum);
        vo.setOrderDiscounts(preferentialVo.getOrderDiscounts());

        List<CustomerCouponVo> customerCoupons = customerCouponService.customerCouponList(new PageRequest(1, 100));
        for (CustomerCouponVo customerCoupon : customerCoupons) {
            Boolean isUsable = customerCouponService.checkCoupon(customerCoupon, shop.getId(), orderProducts);
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
        //自提订单默认距离是0  外卖订单 校验距离 4公里以内
        double distance = getDistance(dto.getCustomerAddressId(), dto.getOrderType(), shop);

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

        //todo 暂时不考虑库存
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

        //订单商品原价
        BigDecimal orderProdOriginalPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (dto.getOrderType() == 1 && shop.getMinDeliveryPrice().compareTo(orderProdOriginalPrice) > 0) {
            throw new PinetException("餐品价格低于" + shop.getMinDeliveryPrice() + "元，无法配送");
        }

        //用户支付的配送费
        BigDecimal shippingFee = getShippingFee(dto.getOrderType(), distance, shop.getDeliveryPlatform());

        //配送费
        BigDecimal shippingFeePlat = getShippingFeePlat(dto.getOrderType(), dto.getCustomerAddressId(), orderProdOriginalPrice, shop.getDeliveryShopNo(), shop.getDeliveryPlatform());

        PreferentialVo preferentialVo = orderPreferentialManager.doPreferential(userId, dto.getCustomerCouponId(), orderProdOriginalPrice, orderProducts);
        BigDecimal orderPrice = BigDecimalUtil.sum(preferentialVo.getProductDiscountAmount(), shippingFee, packageFee);

        //对比订单总金额和结算的总金额  如果不相同说明商品价格有调整
        if (dto.getOrderSource() != 3 && orderPrice.compareTo(dto.getOrderPrice()) != 0) {
            throw new PinetException("订单信息发生变化,请重新下单");
        }

        //判断该订单是否有佣金
        //邀请人必须是店帮主  被邀人不能是店帮主

        //创建订单基础信息
        Orders order = createOrder(dto, shippingFee, distance, orderPrice, preferentialVo.getProductDiscountAmount(), preferentialVo.getDiscountAmount(), shop, packageFee, shippingFeePlat);
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
        List<OrderDiscount> orderDiscounts = preferentialVo.getOrderDiscounts();
        if (!CollectionUtils.isEmpty(orderDiscounts)) {
            orderDiscounts.forEach(item -> item.setOrderId(order.getId()));
            orderDiscountService.saveBatch(orderDiscounts);
        }

        //外卖订单插入订单地址表
        if (dto.getOrderType() == 1) {
            OrderAddress orderAddress = orderAddressService.createByCustomerAddressId(dto.getCustomerAddressId());
            orderAddress.setOrderId(order.getId());
            Customer customer = customerService.getById(order.getCustomerId());
            orderAddress.setSex(customer.getSex());
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

    /**
     * 获取距离
     */
    private double getDistance(Long customerAddressId, Integer orderType, Shop shop) {
        double distance = 0;
        CustomerAddress customerAddress = customerAddressService.getById(customerAddressId);
        if (orderType == 1) {
            if (shop.getSupportDelivery() == 0) {
                throw new PinetException("该店铺暂不支持外卖订单");
            }

            distance = LatAndLngUtils.getDistance(customerAddress.getLng().doubleValue(), customerAddress.getLat().doubleValue(),
                    Double.parseDouble(shop.getLng()), Double.parseDouble(shop.getLat()));
            if (distance > shop.getDeliveryDistance()) {
                throw new PinetException("店铺距离过远,无法配送");
            }
        }
        return distance;

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
        log.info("支付回调查询出订单信息为{}", JSONObject.toJSONString(orders));


        OrderPay orderPay = orderPayService.getByOrderIdAndChannelId(orders.getId(), param.getChannelId());
        orderPay.setPayStatus(2);
        orderPay.setPayTime(param.getPayTime());
        orderPay.setOutTradeNo(param.getOutTradeNo());
        orderPayService.updateById(orderPay);

        //商家该订单收益= 用户支付总金额  - 平台配送费
        BigDecimal shopEarnings = orderPay.getPayPrice().subtract(orders.getShippingFeePlat());

        Integer score = getScore(orders.getCustomerId(), orders.getOrderPrice());
        orders.setScore(score);


        //资金流水
        bCapitalFlowService.add(shopEarnings, orders.getId(), orders.getCreateTime(),
                CapitalFlowWayEnum.getEnumByChannelId(orderPay.getChannelId()), CapitalFlowStatusEnum._1, orders.getShopId());

        //积分流水
        scoreRecordService.addScoreRecord(orders.getShopId(), "消费" + orders.getOrderPrice().toString() + "元",
                score, orders.getId(), ScoreRecordTypeEnum._1, orders.getCustomerId());

        //修改余额 和 积分
        ibUserBalanceService.addAmount(orders.getShopId(), shopEarnings);
        customerBalanceService.addAvailableBalance(orders.getCustomerId(), score);

        //更新优惠券状态
        CustomerCoupon customerCoupon = customerCouponService.getById(orders.getCustomerCouponId());
        if (ObjectUtil.isNotNull(customerCoupon)) {
            customerCoupon.setCouponStatus(4);
            customerCouponService.updateById(customerCoupon);
        }

//        //判断订单状态  如果订单状态是已取消  就退款
//        if (orders.getOrderStatus().equals(OrderStatusEnum.CANCEL.getCode())) {
//
//            IPayService payService = SpringContextUtils.getBean(orderPay.getChannelId() + "_" + "service", IPayService.class);
//            //构造退款记录
//            Snowflake snowflake = IdUtil.getSnowflake();
//
//            OrderRefund orderRefund = new OrderRefund();
//            orderRefund.setRefundNo(snowflake.nextId());
//            orderRefund.setOrderId(orders.getId());
//            orderRefund.setOrderPayId(orderPay.getId());
//            orderRefund.setRefundPrice(orders.getOrderPrice());
//            orderRefund.setOrderPrice(orders.getOrderPrice());
//            orderRefund.setIsAllRefund(true);
//            orderRefund.setRefundDesc("订单超时支付,系统默认退款");
//            orderRefund.setRefundStatus(1);
//            orderRefundService.save(orderRefund);
//
//            //调用退款方法
//            RefundParam refundParam = new RefundParam(orders.getOrderPrice().toString(),
//                    orders.getOrderNo().toString(),
//                    orderRefund.getRefundNo().toString(),
//                    orders.getOrderPrice().toString(),
//                    orderRefund.getId(), orders.getCustomerId());
//            payService.refund(refundParam);
//            orders.setOrderStatus(OrderStatusEnum.REFUND.getCode());
//            return updateById(orders);
//        } else {
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
        log.info("支付回调更新订单信息为{}", JSONObject.toJSONString(orders));
        return updateById(orders);
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

        //积分流水
        scoreRecordService.addScoreRecord(orders.getShopId(), "退款" + orders.getOrderPrice().toString() + "元", -orders.getScore()
                , orders.getId(), ScoreRecordTypeEnum._2, orders.getCustomerId());

        //修改余额
        ibUserBalanceService.addAmount(orders.getShopId(), orderPay.getPayPrice().negate());

        //修改积分
        customerBalanceService.subtractAvailableBalance(orders.getCustomerId(), orders.getScore());


        //退款退回优惠券
        CustomerCoupon customerCoupon = customerCouponService.getById(orders.getCustomerCouponId());
        if (ObjectUtil.isNotNull(customerCoupon)) {
            customerCoupon.setCouponStatus(2);
            customerCouponService.updateById(customerCoupon);
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

    /**
     * 店帮主优惠计算 每个商品分开计算优惠 套餐不参与
     *
     * @param orderProducts  订单商品信息
     * @param orderDiscounts 订单优惠明细
     * @param customerId     用户id
     * @param originalPrice  原价
     * @return 折后价
     */
    private BigDecimal getDiscountedPrice(List<OrderProduct> orderProducts, List<OrderDiscount> orderDiscounts, Long customerId, BigDecimal originalPrice) {
        Integer memberLevel = customerMemberService.getMemberLevel(customerId);
        MemberLevelEnum memberLevelEnum = MemberLevelEnum.getEnumByCode(memberLevel);
        //门客不优惠  直接return
        if (memberLevel.equals(MemberLevelEnum._0.getCode())) {
            return originalPrice;
        }

        boolean isCombo = false;

        for (OrderProduct orderProduct : orderProducts) {
            ShopProduct shopProduct = shopProductService.getById(orderProduct.getShopProdId());
            if ("COMBO".equals(shopProduct.getDishType())) {
                isCombo = true;
            } else {
                orderProduct.setProdPrice(orderProduct.getProdPrice().multiply(memberLevelEnum.getDiscount()).setScale(2, RoundingMode.HALF_UP));
            }
        }
        //计算折后总价
        BigDecimal discountedPrice = orderProducts.stream().map(OrderProduct::getProdPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

        //添加优惠明细
        OrderDiscount orderDiscount = new OrderDiscount();
        String discountMsg = memberLevelEnum.getMsg() + memberLevelEnum.getDiscount().multiply(new BigDecimal(10)) + "优惠";
        orderDiscount.setDiscountMsg(discountMsg)
                .setDiscountAmount(originalPrice.subtract(discountedPrice)).setType(1);
        orderDiscounts.add(orderDiscount);


        if (isCombo) {
            OrderDiscount orderDiscount1 = new OrderDiscount();
            orderDiscount1.setDiscountMsg("特价团餐商品无法参与会员优惠").setDiscountAmount(BigDecimal.ZERO).setType(1);
            orderDiscounts.add(orderDiscount1);
        }

        return discountedPrice;
    }

    /**
     * 获取积分
     *
     * @param customerId 用户id
     * @param orderPrice 订单价格
     * @return 积分
     */
    private Integer getScore(Long customerId, BigDecimal orderPrice) {
        int score = 0;
        Integer memberLevel = customerMemberService.getMemberLevel(customerId);
        //店帮主 2倍积分
        if (memberLevel.equals(MemberLevelEnum._20.getCode())) {
            score = orderPrice.multiply(new BigDecimal("2")).setScale(0, RoundingMode.DOWN).intValue();
        }

        //会员 1倍积分
        if (memberLevel.equals(MemberLevelEnum._10.getCode())) {
            score = orderPrice.multiply(new BigDecimal("1")).setScale(0, RoundingMode.DOWN).intValue();
        }

        //门客 0.5倍积分
        if (memberLevel.equals(MemberLevelEnum._0.getCode())) {
            score = orderPrice.multiply(new BigDecimal("0.5")).setScale(0, RoundingMode.DOWN).intValue();
        }

        return score;
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


    private Orders createOrder(CreateOrderDto dto, BigDecimal shippingFee, Double m, BigDecimal orderPrice, BigDecimal orderProdPrice, BigDecimal discountAmount, Shop shop, BigDecimal packageFee, BigDecimal shippingFeePlat) {
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
        order.setShippingFeePlat(shippingFeePlat);
        order.setCustomerCouponId(dto.getCustomerCouponId());
        return order;
    }


    /**
     * 获取用户支付配送费
     *
     * @param orderType 订单类型( 1外卖  2自提)
     * @return
     */
    private BigDecimal getShippingFee(Integer orderType, double distance, String deliveryPlatform) {
        if (orderType == 2) {
            return new BigDecimal("0");
        }
        if (!Environment.isProd()) {
            return new BigDecimal("4");
        }
        if (deliveryPlatform.equals(DeliveryPlatformEnum.ZPS.getCode())) {
            //todo 商家没有对接外卖平台，自配送
            return BigDecimal.ZERO;
        }


        BigDecimal shippingFee = shippingFeeRuleService.getByDistance(distance);
        if (shippingFee == null) {
            throw new PinetException("配送费查询失败");
        }
        return shippingFee;

    }


    /**
     * 获取平台配送费
     *
     * @param orderType        订单类型( 1外卖  2自提)
     * @param deliveryPlatform 配送平台( ZPS-自配送，DADA-达达)
     * @return BigDecimal
     */
    private BigDecimal getShippingFeePlat(Integer orderType, Long customerAddressId, BigDecimal orderProdPrice, String deliveryShopNo, String deliveryPlatform) {
        if (orderType == 2) {
            return new BigDecimal("0");
        }
        //测试环境默认4元吧
        if (!Environment.isProd()) {
            return new BigDecimal("4");
        }
        if (StringUtil.isBlank(deliveryShopNo) || deliveryPlatform.equals(DeliveryPlatformEnum.ZPS.getCode())) {
            //todo 商家没有对接外卖平台，自配送
            return BigDecimal.ZERO;
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
        log.info("客如云状态变更查询orders:{}", JSONObject.toJSONString(orders));
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
        log.info("客如云状态变更 更新orders:{}", JSONObject.toJSONString(orders));
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

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private ICustomerService customerService;


    @Override
    public void performanceCall(PerformanceCallDTO dto) {
        if (!"OPEN_PLATFORM".equalsIgnoreCase(dto.getOrderSource())) {
            return;
        }
        try {
            QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_no", dto.getOutBizNo());
            Orders orders = getOne(queryWrapper);
            Customer customer = customerService.getById(orders.getCustomerId());
            List<OrderProduct> orderProducts = orderProductService.getByOrderId(orders.getId());

            String prodNames = orderProducts.stream().map(OrderProduct::getProdName).collect(Collectors.joining(",\n"));
            ArrayList<WxMaSubscribeMessage.MsgData> msgDataList = new ArrayList<>(5);
            msgDataList.add(new WxMaSubscribeMessage.MsgData("date15", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, orders.getCreateTime())));//下单时间
            msgDataList.add(new WxMaSubscribeMessage.MsgData("thing11", prodNames));//餐品详情
            msgDataList.add(new WxMaSubscribeMessage.MsgData("amount13", String.valueOf(orders.getOrderPrice())));//订单金额
            msgDataList.add(new WxMaSubscribeMessage.MsgData("character_string19", orders.getMealCode()));//取餐号
            msgDataList.add(new WxMaSubscribeMessage.MsgData("thing7", "您的餐品已制作完成，请到前台领取"));//温馨提醒

            WxMaSubscribeMessage wxMaSubscribeMessage = WxMaSubscribeMessage.builder()
                    .templateId(CommonConstant.PERFORMANCE_CALL_TEMPLATE_ID)
                    .data(msgDataList)
                    .toUser(customer.getQsOpenId())
                    .page("packageA/orderClose/orderDetails?orderId=" + orders.getId())
                    .build();
            wxMaService.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
    }


    /**
     * https://open.keruyun.com/docs/zh/UnJOKokB77V9K553GtMO.html
     * 客如云外卖下单
     *
     * @param order
     * @return
     */
    public String takeoutOrderCreate(Orders order) {
        if (!Environment.isProd()) {
            return "";
        }
        KryOpenTakeoutOrderCreateDTO takeoutOrderCreateDTO = new KryOpenTakeoutOrderCreateDTO();
        takeoutOrderCreateDTO.setOutBizNo(String.valueOf(order.getOrderNo()));
        takeoutOrderCreateDTO.setRemark(order.getRemark());
        takeoutOrderCreateDTO.setOrderSecondSource("WECHAT_MINI_PROGRAM");
        takeoutOrderCreateDTO.setPromoFee(BigDecimalUtil.yuanToFen(order.getDiscountAmount()));//优惠
        takeoutOrderCreateDTO.setActualFee(BigDecimalUtil.yuan2Fen(order.getOrderPrice()));//应付
        takeoutOrderCreateDTO.setTotalFee(BigDecimalUtil.yuanToFen(BigDecimalUtil.sum(order.getOrderPrice(), order.getDiscountAmount())));
        takeoutOrderCreateDTO.setPromoDetailRequestList(getPromoDetailRequestList(order.getId()));

        //todo 打包费暂时传了没用，先把它放到 ExtraFeeRequest 对象里面把
        DcOrderBizRequest dcOrderBizRequest = new DcOrderBizRequest();
        dcOrderBizRequest.setDinnerType("DELIVERY");//外送
//        dcOrderBizRequest.setTakeoutFee(BigDecimalUtil.yuan2Fen(order.getPackageFee()));
//        dcOrderBizRequest.setTableWareFee(0L);
//        dcOrderBizRequest.setTakeMealType("SELF_TAKE");
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
        //配送费
        List<ExtraFeeRequest> extraFeeRequestList = new ArrayList<>();
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
        extraFeeRequestList.add(extraFeeRequest);

        //打包费
        ExtraFeeRequest packageFeeRequest = new ExtraFeeRequest();
        packageFeeRequest.setOutExtraFeeDetailNo(UUID.randomUUID().toString());
        packageFeeRequest.setExtraFeeType("PRACTICE_RAISE_FEE");
        packageFeeRequest.setCustomExtraFeeName("打包费");
        packageFeeRequest.setExtraTotalFee(BigDecimalUtil.yuan2Fen(order.getPackageFee()));
        packageFeeRequest.setExtraActualFee(BigDecimalUtil.yuan2Fen(order.getPackageFee()));
        packageFeeRequest.setExtraPromoFee(0L);
        packageFeeRequest.setExtraCalType("CALCULATE_BY_FIXED");
        packageFeeRequest.setParticipateDiscountFlag(false);
        packageFeeRequest.setParticipateSplitFlag(false);
        packageFeeRequest.setExtraRuleId("");
        extraFeeRequestList.add(packageFeeRequest);
        takeoutOrderCreateDTO.setExtraFeeRequestList(extraFeeRequestList);

        List<OrderProductDto> orderProducts = orderProductService.selectByOrderId(order.getId());
        List<OrderDishRequest> orderDishRequestList = new ArrayList<>(orderProducts.size());
        for (OrderProductDto orderProduct : orderProducts) {
            OrderDishRequest request = new OrderDishRequest();
            request.setOutDishNo(String.valueOf(orderProduct.getOrderProductId()));
            request.setDishId(orderProduct.getProdId());
            request.setDishCode(orderProduct.getDishCode());
            request.setDishName(orderProduct.getProductName());
            request.setDishQuantity(orderProduct.getProdNum());
            request.setDishFee(BigDecimalUtil.yuanToFen(orderProduct.getProdUnitPrice()));
            request.setDishOriginalFee(BigDecimalUtil.yuanToFen(orderProduct.getProdUnitPrice()));
            request.setTotalFee(BigDecimalUtil.multiply(orderProduct.getProdUnitPrice(), orderProduct.getProdNum()));
            request.setActualFee(BigDecimalUtil.yuanToFen(orderProduct.getProdPrice()));
            request.setPromoFee(request.getTotalFee().longValue() - request.getActualFee().longValue());
            request.setUnitId(orderProduct.getUnitId());
            request.setUnitName(orderProduct.getUnit());
            request.setUnitCode(orderProduct.getUnitId());
            request.setDishSkuId(orderProduct.getKrySkuId());
            request.setWeightDishFlag(false);
            request.setDishImgUrl(orderProduct.getProductImg());
            request.setIsPack(true);
            //todo 打包费暂时不用
            request.setPackageFee("0");
            //orderDishRequest.setPackageFee(BigDecimalUtil.yuan2FenStr(orderProduct.getPackageFee()));
            List<ScanCodeDish> dishList = new ArrayList<>();
            if (Objects.equals(DishType.SINGLE, orderProduct.getDishType())) {
                request.setItemOriginType(DishType.SINGLE);
                request.setDishType(DishType.SINGLE_DISH);
                request.setDishAttachPropList(getDishAttachPropList(orderProduct.getOrderProductId()));
            } else if (Objects.equals(DishType.COMBO, orderProduct.getDishType())) {
                request.setItemOriginType(DishType.COMBO);
                request.setDishType(DishType.COMBO_DISH);
                dishList.addAll(getComboGroupDetail(orderProduct, order.getShopId()));
            }
            if (!CollectionUtils.isEmpty(dishList)) {
                request.setDishList(dishList);
            }
            orderDishRequestList.add(request);
        }
        takeoutOrderCreateDTO.setOrderDishRequestList(orderDishRequestList);

        OrderAddress orderAddress = orderAddressService.getOrderAddress(order.getId());
        DeliveryInfoRequest deliveryInfoRequest = new DeliveryInfoRequest();
        deliveryInfoRequest.setDeliveryType("RECIPIENT_ADDRESS");
        deliveryInfoRequest.setOutBizId(String.valueOf(order.getOrderNo()));
        deliveryInfoRequest.setReceiverAddress(orderAddress.getAddress());
        deliveryInfoRequest.setReceiverGender(orderAddress.getSex() == null || orderAddress.getSex() == 0 || orderAddress.getSex() == 1 ? "MAN" : "WOMAN");
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
        if (!Environment.isProd()) {
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
        dto.setPromoDetailRequestList(getPromoDetailRequestList(orders.getId()));

        List<OrderProductDto> orderProducts = orderProductService.selectByOrderId(orders.getId());
        List<OrderDishRequest> orderDishRequestList = new ArrayList<>();
        for (OrderProductDto orderProduct : orderProducts) {
            OrderDishRequest request = new OrderDishRequest();
            request.setOutDishNo(String.valueOf(orderProduct.getOrderProductId()));
            request.setDishId(orderProduct.getProdId());
            request.setDishName(orderProduct.getProductName());
            request.setDishCode(orderProduct.getDishCode());
            request.setDishQuantity(orderProduct.getProdNum());
            request.setDishFee(BigDecimalUtil.yuanToFen(orderProduct.getProdUnitPrice()));
            request.setDishOriginalFee(BigDecimalUtil.yuanToFen(orderProduct.getProdUnitPrice()));
            request.setTotalFee(BigDecimalUtil.multiply(orderProduct.getProdUnitPrice(), orderProduct.getProdNum()));
            request.setActualFee(BigDecimalUtil.yuanToFen(orderProduct.getProdPrice()));
            request.setPromoFee(request.getTotalFee().longValue() - request.getActualFee().longValue());
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
            if (Objects.equals(DishType.SINGLE, orderProduct.getDishType())) {
                //配料明细
                //附加项（加料、做法）列表
                request.setDishType(DishType.SINGLE_DISH);
                request.setItemOriginType(DishType.SINGLE);
                request.setDishAttachPropList(getDishAttachPropList(orderProduct.getOrderProductId()));
            } else if (Objects.equals(DishType.COMBO, orderProduct.getDishType())) {
                request.setDishType(DishType.COMBO_DISH);
                request.setItemOriginType(DishType.COMBO);
                //套餐明细
                dishList.addAll(getComboGroupDetail(orderProduct, orders.getShopId()));
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
     * 优惠明细
     *
     * @param orderId
     * @return
     */
    private List<PromoDetailRequest> getPromoDetailRequestList(Long orderId) {
        QueryWrapper<OrderDiscount> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        queryWrapper.gt("discount_amount", 0);
        List<OrderDiscount> orderDiscounts = orderDiscountService.list(queryWrapper);
        if (CollectionUtils.isEmpty(orderDiscounts)) {
            return null;
        }

        List<PromoDetailRequest> promoDetailRequestList = new ArrayList<>(orderDiscounts.size());
        for (OrderDiscount orderDiscount : orderDiscounts) {
            PromoDetailRequest promoDetailRequest = new PromoDetailRequest();
            promoDetailRequest.setOutPromoDetailId(UUID.randomUUID().toString());
            promoDetailRequest.setPromoId(String.valueOf(orderDiscount.getId()));
            promoDetailRequest.setPromoName(orderDiscount.getDiscountMsg());
            promoDetailRequest.setPromoFee(BigDecimalUtil.yuan2Fen(orderDiscount.getDiscountAmount()));
            promoDetailRequest.setPromoCategory("ORDER_DIMENSION");
            promoDetailRequest.setPromoType("THIRD_MERCHANT");
            promoDetailRequest.setPromoDimension("TOATL_CART");
            promoDetailRequestList.add(promoDetailRequest);
        }
        return promoDetailRequestList;

    }

    /**
     * 套餐明细
     *
     * @param orderProduct
     * @param shopId
     */
    private List<ScanCodeDish> getComboGroupDetail(OrderProductDto orderProduct, Long shopId) {
        List<KryComboGroupDetailVo> kryComboGroupDetailList = kryComboGroupDetailService.getByOrderProdId(orderProduct.getOrderProductId(), shopId);
        List<ScanCodeDish> dishList = new ArrayList<>(kryComboGroupDetailList.size());
        for (KryComboGroupDetailVo groupDetail : kryComboGroupDetailList) {
            ScanCodeDish dish = new ScanCodeDish();
            dish.setOutDishNo(String.valueOf(groupDetail.getId()));
            dish.setOutDishNo(UUID.randomUUID().toString());
            dish.setDishId(groupDetail.getSingleDishId());
            dish.setDishType("COMBO_DETAIL");
            dish.setDishCode(groupDetail.getDishCode());
            dish.setDishName(groupDetail.getDishName());
            dish.setDishQuantity(BigDecimal.ONE);
            dish.setDishFee(groupDetail.getSellPrice());
            dish.setUnitId(groupDetail.getUnitId());
            dish.setUnitCode(groupDetail.getUnitId());
            dish.setUnitName(groupDetail.getUnit());
            dish.setDishOriginalFee(groupDetail.getSellPrice());
            dish.setTotalFee(groupDetail.getSellPrice() * dish.getDishQuantity().longValue());
            dish.setActualFee(groupDetail.getPrice() * dish.getDishQuantity().longValue());
            dish.setPromoFee(dish.getTotalFee() - dish.getActualFee());
            dish.setPackageFee("0");
            dish.setWeightDishFlag("0");
            dish.setDishImgUrl(groupDetail.getImageUrl());
            dish.setIsPack("false");
            dish.setDishGiftFlag("false");
            dish.setItemOriginType("SINGLE");
            dish.setDishSkuId(groupDetail.getDishSkuId());
            dishList.add(dish);
        }
        return dishList;
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
