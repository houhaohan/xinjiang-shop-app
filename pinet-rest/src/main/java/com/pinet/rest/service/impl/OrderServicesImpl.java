package com.pinet.rest.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.DB;
import com.pinet.core.constants.OrderConstant;
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
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.dto.*;
import com.pinet.rest.entity.enums.*;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.entity.param.OrderRefundNotifyParam;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.param.RefundParam;
import com.pinet.rest.entity.vo.*;
import com.pinet.rest.handler.order.OrderContext;
import com.pinet.rest.handler.settle.DishSettleContext;
import com.pinet.rest.handler.settle.OrderSetterContext;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.*;
import com.pinet.rest.strategy.MemberLevelStrategyContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
@RequiredArgsConstructor
public class OrderServicesImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {
    private final IOrderProductService orderProductService;
    private final IOrderAddressService orderAddressService;
    private final IShopService shopService;
    private final ICartService cartService;
    private final JmsUtil jmsUtil;
    private final IOrderPayService orderPayService;
    private final IOrderProductSpecService orderProductSpecService;
    private final IOrderRefundService orderRefundService;
    private final ICustomerMemberService customerMemberService;
    private final IBCapitalFlowService bCapitalFlowService;
    private final IBUserBalanceService ibUserBalanceService;
    private final ICustomerCouponService customerCouponService;
    private final IOrderDiscountService orderDiscountService;
    private final IKryApiService kryApiService;
    private final IKryOrderCompensateService kryOrderCompensateService;
    private final IKryOrderPushLogService kryOrderPushLogService;
    private final IDaDaService daDaService;
    private final ICustomerAddressService customerAddressService;
    private final IScoreRecordService scoreRecordService;
    private final ICustomerBalanceService customerBalanceService;
    private final OrderPreferentialManager orderPreferentialManager;
    private final OrderContext context;
    private final DishSettleContext dishSettleContext;
    private final IShopProductSpecService shopProductSpecService;
    private final IOrderComboDishService orderComboDishService;
    private final IOrderSideService orderSideService;
    private final WechatTemplateMessageDeliver wechatTemplateMessageDeliver;
    private final IVipUserService vipUserService;
    private final RedisUtil redisUtil;


    @Override
    public List<OrderListVo> orderList(OrderListDto dto) {

        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Page<OrderListVo> page = new Page<>(dto.getPageNum(), dto.getPageSize());

        IPage<OrderListVo> orderListVos = baseMapper.selectOrderList(page, customerId);
        orderListVos.getRecords().forEach(k -> {
            k.setOrderStatusStr(OrderStatusEnum.getEnumByCode(k.getOrderStatus()));
            //如果是自提订单并且是配送中 修改状态状态str为可领取
            if (k.getOrderStatus().equals(OrderStatusEnum.SEND_OUT.getCode())
                    && Objects.equals(k.getOrderType(), OrderTypeEnum.SELF_PICKUP.getCode())) {
                k.setOrderStatusStr("可领取");
            }
            List<OrderProduct> orderProducts = new ArrayList<>();
            List<OrderProduct> singleOrderProducts = orderProductService.getByOrderId(k.getOrderId());
            List<OrderProduct> comboOrderProducts = orderProductService.getComboByOrderId(k.getOrderId());
            orderProducts.addAll(singleOrderProducts);
            orderProducts.addAll(comboOrderProducts);
            k.setOrderProducts(orderProducts);
            k.setProdNum(orderProducts.size());
        });
        return orderListVos.getRecords();
    }


    @Override
    public OrderDetailVo orderDetail(Long orderId) {
        OrderDetailVo orderDetailVo = baseMapper.selectOrderDetail(orderId);
        if (orderDetailVo == null) {
            throw new PinetException("订单不存在");
        }

        List<OrderProduct> singleOrderProducts = orderProductService.getByOrderId(orderId);
        List<OrderProduct> comboOrderProducts = orderProductService.getComboByOrderId(orderId);
        List<OrderProduct> orderProducts = new ArrayList<>(singleOrderProducts.size() + comboOrderProducts.size());
        orderProducts.addAll(singleOrderProducts);
        orderProducts.addAll(comboOrderProducts);
        orderDetailVo.setOrderProducts(orderProducts);
        //判断是自提还是外卖
        if (Objects.equals(orderDetailVo.getOrderType(), OrderTypeEnum.TAKEAWAY.getCode())) {
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


    /**
     * 订单结算重构
     *
     * @param dto
     * @return
     */
    @Override
    public OrderSettlementVo orderSettlement(OrderSettlementDto dto) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Shop shop = shopService.getById(dto.getShopId());
        //判断店铺是否营业
        checkShop(shop);
        if (Objects.equals(dto.getOrderType(), OrderTypeEnum.TAKEAWAY.getCode())
                && Objects.equals(shop.getSupportDelivery(), CommonConstant.NO)) {
            throw new PinetException("该门店暂不支持外卖");
        }
        Double distance = getDistance(dto.getCustomerAddressId(), dto.getOrderType(), shop);

        OrderSettlementVo vo = new OrderSettlementVo();
        vo.setShopName(shop.getShopName());
        OrderSetterContext orderSetterContext = new OrderSetterContext();
        orderSetterContext.setCartService(cartService);
        dishSettleContext.setRequest(dto);
        orderSetterContext.setDishSettleContext(dishSettleContext);
        orderSetterContext.setUserId(customerId);
        orderSetterContext.setDistance(distance);
        orderSetterContext.setDeliveryPlatform(shop.getDeliveryPlatform());
        orderSetterContext.execute();
        List<OrderProduct> orderProducts = orderSetterContext.getResponse();

        vo.setPackageFee(orderSetterContext.getPackageFee());
        vo.setOrderProductBoList(orderProducts);
        vo.setOrderMakeCount(countShopOrderMakeNum(dto.getShopId()));

        //配送费
        vo.setShippingFee(orderSetterContext.getShippingFee());

        //设置订单原价 和 商品原价
        vo.setOriginalOrderProductPrice(orderSetterContext.getOrderProdPrice());
        vo.setOriginalPrice(BigDecimalUtil.sum(orderSetterContext.getOrderProdPrice(), orderSetterContext.getShippingFee(), orderSetterContext.getPackageFee()));

        //订单优惠处理
        PreferentialVo preferentialVo = orderPreferentialManager.doPreferential(customerId, dto.getCustomerCouponId(), orderSetterContext.getOrderProdPrice(), orderProducts);
        vo.setOrderPrice(BigDecimalUtil.sum(preferentialVo.getProductDiscountAmount(), orderSetterContext.getPackageFee(), orderSetterContext.getShippingFee()));

        //返回预计送达时间
        Date now = new Date();
        String estimateArrivalStartTime = DateUtil.format(DateUtil.offsetMinute(now, 15), "HH:mm");
        String estimateArrivalEndTime = DateUtil.format(DateUtil.offsetMinute(now, 45), "HH:mm");
        vo.setEstimateArrivalTime(estimateArrivalStartTime + "-" + estimateArrivalEndTime);

        vo.setOrderProductNum(orderSetterContext.getOrderProductNum());
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
        return baseMapper.countShopOrderMakeNum(shopId, queryDate);
    }


    @DSTransactional
    @Override
    public CreateOrderVo createOrder(CreateOrderDto request) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        String redisKey = "qingshi:order:repetition:user_id:"+userId;
        boolean lock = redisUtil.setIfAbsent(redisKey, "1", 5, TimeUnit.SECONDS);
        if(!lock){
            throw new PinetException("您点击的太快啦，请稍后重试！");
        }
        //店铺是否营业
        Shop shop = shopService.getById(request.getShopId());
        checkShop(shop);

        context.setCustomerId(userId);
        context.setShop(shop);
        context.setRequest(request);
        context.setDistance(getDistance(request.getCustomerAddressId(), request.getOrderType(), shop));
        context.handler();
        return context.getResponse();
    }


    /**
     * 获取距离
     */
    private double getDistance(Long customerAddressId, Integer orderType, Shop shop) {
        CustomerAddress customerAddress = customerAddressService.getById(customerAddressId);
        if (!Objects.equals(orderType, OrderTypeEnum.TAKEAWAY.getCode())) {
            return 0D;
        }
        if (Objects.equals(shop.getSupportDelivery(), CommonConstant.NO)) {
            throw new PinetException("该店铺暂不支持外卖订单");
        }
        double distance = LatAndLngUtils.getDistance(customerAddress.getLng().doubleValue(), customerAddress.getLat().doubleValue(),
                Double.parseDouble(shop.getLng()), Double.parseDouble(shop.getLat()));
        if (distance > shop.getDeliveryDistance()) {
            throw new PinetException("店铺距离过远,无法配送");
        }
        return distance;
    }


    private void checkShop(Shop shop) {
        //判断店铺是否营业
        if (!shopService.checkShopStatus(shop)) {
            throw new PinetException("店铺已经打烊了~");
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
        if (Objects.isNull(orders)) {
            throw new PinetException("订单不存在");
        }
        if (BigDecimalUtil.ne(orders.getOrderPrice(), dto.getOrderPrice())) {
            throw new PinetException("支付金额异常,请重新支付");
        }
        //根据不同支付渠道获取调用不同支付方法
        IPayService payService = SpringContextUtils.getBean(dto.getChannelId() + "_" + "service", IPayService.class);
        OrderPay orderPay = orderPayService.getByOrderIdAndChannelId(orders.getId(), dto.getChannelId());
        if (Objects.nonNull(orderPay) && Objects.equals(orderPay.getPayStatus(), OrderConstant.PAID)) {
            throw new PinetException("订单已支付");
        }

        //构造orderPay
        orderPay = new OrderPay();
        orderPay.setOrderId(orders.getId());
        orderPay.setOrderNo(orders.getOrderNo());
        orderPay.setCustomerId(customerId);
        orderPay.setPayStatus(OrderConstant.UNPAID);
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
        return payService.pay(param);
    }

    @Override
    @DSTransactional
    public Boolean orderPayNotify(OrderPayNotifyParam param) {
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getOrderNo, param.getOrderNo());
        Orders orders = getOne(lambdaQueryWrapper);
        if (orders == null) {
            log.error("微信支付回调出现异常,订单号不存在:" + param.getOrderNo());
            return false;
        }
        orders.setOrderStatus(OrderStatusEnum.PAY_COMPLETE.getCode());
        log.info("支付回调查询出订单信息为{}", JSONObject.toJSONString(orders));

        OrderPay orderPay = orderPayService.getByOrderIdAndChannelId(orders.getId(), param.getChannelId());
        orderPay.setPayStatus(OrderConstant.PAID);
        orderPay.setPayTime(param.getPayTime());
        orderPay.setOutTradeNo(param.getOutTradeNo());
        orderPayService.updateById(orderPay);

        //商家该订单收益= 用户支付总金额  - 平台配送费
        BigDecimal shopEarnings = BigDecimalUtil.subtract(orderPay.getPayPrice(), orders.getShippingFeePlat());
        Integer memberLevel = customerMemberService.getMemberLevel(orders.getCustomerId());
        Integer score = new MemberLevelStrategyContext(orders.getOrderPrice()).getScore(memberLevel);
        orders.setScore(score);

        //资金流水
        bCapitalFlowService.add(shopEarnings, orders.getId(), orders.getCreateTime(),
                CapitalFlowWayEnum.getEnumByChannelId(orderPay.getChannelId()), CapitalFlowStatusEnum.SUCCESS, orders.getShopId());

        //积分流水
        scoreRecordService.addScoreRecord(orders.getShopId(), "消费" + orders.getOrderPrice().toString() + "元",
                score, orders.getId(), ScoreRecordTypeEnum.ORDER, orders.getCustomerId());

        //修改余额 和 积分
        ibUserBalanceService.addAmount(orders.getShopId(), shopEarnings);
        customerBalanceService.addAvailableBalance(orders.getCustomerId(), score);

        //更新优惠券状态
        CustomerCoupon customerCoupon = customerCouponService.getById(orders.getCustomerCouponId());
        if (Objects.nonNull(customerCoupon)) {
            customerCoupon.setCouponStatus(CouponReceiveStatusEnum.USED.getCode());
            customerCouponService.updateById(customerCoupon);
        }
        updateById(orders);

        //更新VIP等级
        BigDecimal paidAmount = baseMapper.getPaidAmount(orders.getCustomerId());
//        vipUserService.getByCustomerId()


        //推送客如云,异步处理
        jmsUtil.sendMsgQueue(QueueConstants.KRY_ORDER_PUSH, String.valueOf(orders.getId()));
        log.info("支付回调更新订单信息为{}", JSONObject.toJSONString(orders));
        return true;
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
        return updateById(orders);
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
                CapitalFlowWayEnum.getEnumByChannelId(orderPay.getChannelId()), CapitalFlowStatusEnum.REFUND, orders.getShopId());

        //积分流水
        scoreRecordService.addScoreRecord(orders.getShopId(), "退款" + orders.getOrderPrice().toString() + "元", -orders.getScore()
                , orders.getId(), ScoreRecordTypeEnum.REFUND, orders.getCustomerId());

        //修改余额
        ibUserBalanceService.addAmount(orders.getShopId(), orderPay.getPayPrice().negate());

        //修改积分
        customerBalanceService.subtractAvailableBalance(orders.getCustomerId(), orders.getScore());


        //退款退回优惠券
        CustomerCoupon customerCoupon = customerCouponService.getById(orders.getCustomerCouponId());
        if (Objects.nonNull(customerCoupon)) {
            customerCoupon.setCouponStatus(CouponReceiveStatusEnum.RECEIVED.getCode());
            customerCouponService.updateById(customerCoupon);
        }

        orderRefund.setRefundStatus(OrderConstant.NOT_RECEIVED);
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
        //单品
        List<OrderProduct> orderProducts = orderProductService.getByOrderId(orderId);
        orderProducts.forEach(k -> {
            AddCartDTO addCartDto = new AddCartDTO();
            addCartDto.setShopId(order.getShopId());
            addCartDto.setShopProdId(k.getShopProdId());
            addCartDto.setProdNum(k.getProdNum());
            addCartDto.setCustomerId(customerId);
            String shopProdSpecIds = k.getOrderProductSpecs().stream().map(OrderProductSpec::getShopProdSpecId).map(String::valueOf).collect(Collectors.joining(","));
            addCartDto.setShopProdSpecIds(shopProdSpecIds);
            addCartDto.setSideDishGroupList(k.getSideDishGroupList());
            cartService.addCart(addCartDto);
        });

        //套餐
        List<OrderProduct> comboProducts = orderProductService.getComboByOrderId(orderId);
        comboProducts.forEach(p -> {
            AddCartDTO addCartDto = new AddCartDTO();
            addCartDto.setShopId(order.getShopId());
            addCartDto.setShopProdId(p.getShopProdId());
            addCartDto.setProdNum(p.getProdNum());
            addCartDto.setCustomerId(customerId);
            List<AddCartDTO.CartComboDishDTO> comboDetails = p.getComboDishDetails().stream().map(item -> {
                AddCartDTO.CartComboDishDTO dto = new AddCartDTO.CartComboDishDTO();
                dto.setShopProdId(p.getShopProdId());
                dto.setSingleProdId(item.getSingleDishId());
                String shopProdSpecIds = item.getOrderProductSpecs().stream().map(spec -> String.valueOf(spec.getShopProdSpecId())).collect(Collectors.joining(","));
                dto.setShopProdSpecIds(shopProdSpecIds);
                return dto;
            }).collect(Collectors.toList());
            addCartDto.setComboDetails(comboDetails);
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

    @Override
    public void performanceCall(PerformanceCallDTO dto) {
        if (!"OPEN_PLATFORM".equalsIgnoreCase(dto.getOrderSource())) {
            return;
        }
        try {
            QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("order_no", dto.getOutBizNo());
            Orders orders = getOne(queryWrapper);
            List<OrderProduct> orderProducts = orderProductService.getByOrderId(orders.getId());

            String prodNames = orderProducts.stream().map(OrderProduct::getProdName).collect(Collectors.joining(",\n"));
            ArrayList<WxMaSubscribeMessage.MsgData> data = new ArrayList<>(5);
            data.add(new WxMaSubscribeMessage.MsgData("date15", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, orders.getCreateTime())));//下单时间
            data.add(new WxMaSubscribeMessage.MsgData("thing11", prodNames));//餐品详情
            data.add(new WxMaSubscribeMessage.MsgData("amount13", String.valueOf(orders.getOrderPrice())));//订单金额
            data.add(new WxMaSubscribeMessage.MsgData("character_string19", orders.getMealCode()));//取餐号
            data.add(new WxMaSubscribeMessage.MsgData("thing7", "您的餐品已制作完成，请到前台领取"));//温馨提醒

            String templateId = WeChatTemplateEnum.PERFORMANCE_CALL.getKey();
            String url = WeChatTemplateEnum.PERFORMANCE_CALL.getPageUrl();
            wechatTemplateMessageDeliver.asyncSend(templateId,url,orders.getCustomerId(),data);
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
    @Override
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

        PaymentDetailRequest paymentDetailRequest = new PaymentDetailRequest();
        paymentDetailRequest.setOutBizId(IdUtil.getSnowflake().nextIdStr());
        paymentDetailRequest.setAmount(BigDecimalUtil.yuanToFen(order.getOrderPrice()));
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
        orderProducts.sort(Comparator.comparing(OrderProductDto::getOrderProductId));
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
            BigDecimal unitPrice = BigDecimalUtil.sum(orderProduct.getProdUnitPrice(), orderProduct.getSidePrice());
            request.setTotalFee(BigDecimalUtil.yuanToFen(unitPrice) * orderProduct.getProdNum());
            request.setActualFee(BigDecimalUtil.yuanToFen(orderProduct.getProdPrice()));
            request.setPromoFee(request.getTotalFee().longValue() - request.getActualFee().longValue());
            request.setUnitId(orderProduct.getUnitId());
            request.setUnitName(orderProduct.getUnit());
            request.setUnitCode(orderProduct.getUnitId());
            request.setDishSkuId(orderProduct.getKrySkuId());
            request.setWeightDishFlag(false);
            request.setDishImgUrl(orderProduct.getProductImg());
            request.setIsPack(true);
            request.setPackageFee("0");
            if (Objects.equals(DishType.SINGLE, orderProduct.getDishType())) {
                request.setItemOriginType(DishType.SINGLE);
                request.setDishType(DishType.SINGLE_DISH);
                request.setDishAttachPropList(getDishAttachPropList(orderProduct.getOrderProductId()));
                request.setDishList(getSideDishList(orderProduct.getOrderProductId(), order.getShopId()));
            } else if (Objects.equals(DishType.COMBO, orderProduct.getDishType())) {
                request.setItemOriginType(DishType.COMBO);
                request.setDishType(DishType.COMBO_DISH);
                request.setDishList(getComboGroupDetail(orderProduct));
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
        orderProducts.sort(Comparator.comparing(OrderProductDto::getOrderProductId));
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
            BigDecimal unitPrice = BigDecimalUtil.sum(orderProduct.getProdUnitPrice(), orderProduct.getSidePrice());
            request.setTotalFee(BigDecimalUtil.yuanToFen(unitPrice) * orderProduct.getProdNum());
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

            //配料明细 或者 套餐明细
            if (Objects.equals(DishType.SINGLE, orderProduct.getDishType())) {
                request.setDishType(DishType.SINGLE_DISH);
                request.setItemOriginType(DishType.SINGLE);
                //做法
                request.setDishAttachPropList(getDishAttachPropList(orderProduct.getOrderProductId()));
                //小料
                request.setDishList(getSideDishList(orderProduct.getOrderProductId(), orders.getShopId()));
            } else if (Objects.equals(DishType.COMBO, orderProduct.getDishType())) {
                request.setDishType(DishType.COMBO_DISH);
                request.setItemOriginType(DishType.COMBO);
                //套餐明细
                request.setDishList(getComboGroupDetail(orderProduct));
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
     * @param
     */
    private List<ScanCodeDish> getComboGroupDetail(OrderProductDto orderProduct) {
        List<OrderComboDishVo> orderComboDishList = orderComboDishService.getByOrderIdAndOrderProdId(orderProduct.getOrderId(), orderProduct.getOrderProductId());
        if (CollectionUtils.isEmpty(orderComboDishList)) {
            return null;
        }
        Map<String, List<OrderComboDishVo>> singleOrderMap = orderComboDishList.stream().collect(Collectors.groupingBy(OrderComboDishVo::getSingleDishId, LinkedHashMap::new, Collectors.toList()));

        List<ScanCodeDish> dishList = new ArrayList<>(singleOrderMap.size());
        for (Map.Entry<String, List<OrderComboDishVo>> entry : singleOrderMap.entrySet()) {
            List<OrderComboDishVo> orderComboDishVoList = entry.getValue();
            OrderComboDishVo orderComboDishVo = orderComboDishVoList.get(0);
            ScanCodeDish dish = new ScanCodeDish();
            dish.setOutDishNo(IdUtil.getSnowflake().nextIdStr());
            dish.setDishId(entry.getKey());
            dish.setDishType(DishType.COMBO_DISH);
            dish.setDishCode(orderComboDishVo.getDishCode());
            dish.setDishName(orderComboDishVo.getSingleProdName());
            //做法
            List<DishAttachProp> dishAttachPropList = orderComboDishVoList.stream()
                    .filter(o -> !Objects.equals("标准", o.getShopProdSpecName()))
                    .map(o -> {
                        DishAttachProp dishAttachProp = new DishAttachProp();
                        dishAttachProp.setOutAttachPropNo(IdUtil.getSnowflake().nextIdStr());
                        dishAttachProp.setAttachPropType("PRACTICE");
                        dishAttachProp.setAttachPropCode(dishAttachProp.getOutAttachPropNo());
                        dishAttachProp.setAttachPropName(o.getShopProdSpecName());
                        dishAttachProp.setPrice(BigDecimalUtil.yuan2Fen(o.getAddPrice()));
                        dishAttachProp.setQuantity(1);
                        dishAttachProp.setTotalFee(dishAttachProp.getPrice() * dishAttachProp.getQuantity());
                        dishAttachProp.setPromoFee(0L);
                        dishAttachProp.setActualFee(dishAttachProp.getPrice() * dishAttachProp.getQuantity());
                        dishAttachProp.setAttachPropId(dishAttachProp.getOutAttachPropNo());
                        return dishAttachProp;
                    }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(dishAttachPropList)) {
                dish.setDishAttachPropList(dishAttachPropList);
            }
            dish.setDishQuantity(BigDecimal.ONE);
            dish.setDishFee(0L);
            dish.setUnitId(orderComboDishVo.getUnitId());
            dish.setUnitCode(orderComboDishVo.getUnitId());
            dish.setUnitName(orderComboDishVo.getUnit());
            dish.setDishOriginalFee(dish.getDishFee());
            dish.setTotalFee(dish.getDishFee() * dish.getDishQuantity().longValue());//菜品总金额
            dish.setActualFee(dish.getDishFee() * dish.getDishQuantity().longValue());//应付金额
            dish.setPromoFee(dish.getTotalFee() - dish.getActualFee());//优惠
            dish.setPackageFee("0");
            dish.setWeightDishFlag("0");
            dish.setDishImgUrl(orderComboDishVo.getImageUrl());
            dish.setIsPack("false");
            dish.setDishGiftFlag("false");
            dish.setItemOriginType(DishType.SINGLE);
            String dishSkuId = orderComboDishVoList.stream()
                    .filter(o -> StringUtil.isNotBlank(o.getDishSkuId()))
                    .map(OrderComboDishVo::getDishSkuId)
                    .findFirst()
                    .orElseGet(() -> {
                        List<ShopProductSpec> shopProductSpecList = shopProductSpecService.getByShopProdId(orderComboDishVo.getSingleProdId());
                        return shopProductSpecList.stream()
                                .filter(spec -> StringUtil.isBlank(spec.getCookingWayId()))
                                .map(ShopProductSpec::getKrySkuId)
                                .findFirst()
                                .get();
                    });
            dish.setDishSkuId(dishSkuId);
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
        //做法
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
     * 获取订单小料
     *
     * @param orderProdId
     * @param shopId
     * @return
     */
    private List<ScanCodeDish> getSideDishList(Long orderProdId, Long shopId) {
        List<OrderSideVo> orderSideList = orderSideService.getByOrderProdIdAndShopId(orderProdId, shopId);
        if (CollectionUtils.isEmpty(orderSideList)) {
            return null;
        }
        List<ScanCodeDish> sideDishList = new ArrayList<>();
        for (OrderSideVo orderSide : orderSideList) {
            ScanCodeDish side = new ScanCodeDish();
            side.setOutDishNo(IdUtil.getSnowflake().nextIdStr());
            side.setDishId(orderSide.getSideDishId());
            side.setDishName(orderSide.getSideDishName());
            side.setDishCode(orderSide.getDishCode());
            side.setDishQuantity(new BigDecimal(orderSide.getQuantity()));
            side.setDishFee(BigDecimalUtil.yuan2Fen(orderSide.getAddPrice()));
            side.setDishOriginalFee(BigDecimalUtil.yuan2Fen(orderSide.getAddPrice()));
            side.setTotalFee(BigDecimalUtil.yuan2Fen(orderSide.getTotalPrice()));
            side.setActualFee(BigDecimalUtil.yuan2Fen(orderSide.getTotalPrice()));
            side.setPromoFee(0L);
            side.setPackageFee("0");
            side.setDishSkuId(orderSide.getDishSkuId());
            side.setDishSkuCode(orderSide.getDishSkuCode());
            side.setDishSkuName(orderSide.getDishSkuName());
            side.setWeightDishFlag("false");
            side.setUnitCode(orderSide.getUnitId());
            side.setUnitId(orderSide.getUnitId());
            side.setUnitName(orderSide.getUnitName());
            side.setDishType(DishType.ADDITIONAL_ITEM);
            side.setItemOriginType(DishType.SIDE);
            side.setIsPack("false");
            side.setIsFixAdditionalItemQuantityFlag(false);
            sideDishList.add(side);
        }
        return sideDishList;
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
    @Async
    public void pushKryOrderLog(Long orderId, String param, String res, String success) {
        KryOrderPushLog log = new KryOrderPushLog();
        log.setOrderId(orderId);
        log.setParams(param);
        log.setPushTime(new Date());
        log.setPushRes(res);
        log.setStatus("fail".equals(success) ? 0 : 1);
        kryOrderPushLogService.save(log);
    }

}
