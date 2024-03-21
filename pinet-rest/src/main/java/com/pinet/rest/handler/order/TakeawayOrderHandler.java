package com.pinet.rest.handler.order;//package com.pinet.rest.handler.orders;
//
//
//import cn.hutool.core.lang.Snowflake;
//import cn.hutool.core.util.IdUtil;
//import cn.hutool.core.util.ObjectUtil;
//import com.imdada.open.platform.client.internal.req.order.AddOrderReq;
//import com.imdada.open.platform.client.internal.resp.order.AddOrderResp;
//import com.imdada.open.platform.exception.RpcException;
//import com.pinet.core.exception.PinetException;
//import com.pinet.core.util.*;
//import com.pinet.keruyun.openapi.constants.DishType;
//import com.pinet.rest.entity.*;
//import com.pinet.rest.entity.enums.*;
//import com.pinet.rest.entity.vo.PreferentialVo;
//import com.pinet.rest.service.*;
//import com.pinet.rest.strategy.MemberLevelStrategyContext;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.CollectionUtils;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
///**
// * @description: 外卖单
// * @author: chengshuanghui
// * @date: 2024-03-09 10:23
// */
//@Component
//@RequiredArgsConstructor
//public class TakeawayOrderHandler extends OrderAbstractHandler {
//    private final ICustomerAddressService customerAddressService;
//    private final OrderPreferentialManager orderPreferentialManager;
//    private final IShippingFeeRuleService shippingFeeRuleService;
//    private final ICustomerMemberService customerMemberService;
//    private final IDaDaService daDaService;
//    private final IOrdersService ordersService;
//    private final IShopProductService shopProductService;
//    private final OrderComboDishHandler orderComboDishHandler;
//    private final OrderSingleDishHandler orderSingleDishHandler;
//    private final IOrderAddressService orderAddressService;
//    private final IOrderDiscountService orderDiscountService;
//    private final ICustomerService customerService;
//
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void create() {
//        Orders orders = buildOrder();
//        orders.setShippingFeePlat(getShippingFeePlat(orders.getOrderProdPrice()));
//        Integer level = customerMemberService.getMemberLevel(orders.getCustomerId());
//        context.orderUserLevel = level;
//        Integer score = new MemberLevelStrategyContext(orders.getOrderPrice()).getScore(level);
//        orders.setScore(score);
//        //初始化订单金额
//        orders.setOrderPrice(BigDecimal.ZERO);
//        orders.setPackageFee(BigDecimal.ZERO);
//        orders.setOrderProdPrice(BigDecimal.ZERO);
//        orders.setCommission(BigDecimal.ZERO);
//        ordersService.save(orders);
//        boolean condition = commissionCondition(orders.getCustomerId(), orders.getShareId());
//
//        List<OrderProduct> orderProducts = new ArrayList<>();
//        for(Cart cart : context.cartList){
//            ShopProduct shopProduct = shopProductService.getById(cart.getShopProdId());
//            OrderProductRequest request = OrderProductRequest.builder()
//                    .cartId(cart.getId())
//                    .dishId(cart.getDishId())
//                    .orderId(orders.getId())
//                    .shopProdId(cart.getShopProdId())
//                    .prodName(shopProduct.getProductName())
//                    .prodNum(cart.getProdNum())
//                    .prodImg(shopProduct.getProductImg())
//                    .unit(shopProduct.getUnit())
//                    .calculate(condition)
//                    .build();
//            OrderProduct orderProduct = null;
//            if(Objects.equals(cart.getDishType(), DishType.SINGLE)){
//                orderProduct = orderSingleDishHandler.exectue(request);
//            }else {
//                orderProduct = orderComboDishHandler.exectue(request);
//            }
//
//            orders.setPackageFee(BigDecimalUtil.sum(orders.getPackageFee(),orderProduct.getPackageFee()));
//            orders.setOrderProdPrice(BigDecimalUtil.sum(orders.getOrderProdPrice(),orderProduct.getProdPrice()));
//            orderProducts.add(orderProduct);
//        }
//
//
//
//        PreferentialVo preferentialVo = orderPreferentialManager.doPreferential(context.customerId, context.request.getCustomerCouponId(), orders.getOrderProdPrice(), orderProducts);
//        orders.setDiscountAmount(preferentialVo.getDiscountAmount());
//        BigDecimal orderPrice = BigDecimalUtil.sum(preferentialVo.getProductDiscountAmount(), orders.getShippingFee(), orders.getPackageFee());
//        //对比订单总金额和结算的总金额  如果不相同说明商品价格有调整
//        if (!Objects.equals(orders.getOrderSource(), OrderSourceEnum.SYSTEM.getCode())
//                && BigDecimalUtil.ne(orderPrice,orders.getOrderPrice())) {
//            throw new PinetException("订单信息发生变化,请重新下单");
//        }
//        orders.setCommission(BigDecimalUtil.multiply(orders.getOrderPrice(),0.1));
//        ordersService.updateById(orders);
//
//        //插入优惠明细表
//        List<OrderDiscount> orderDiscounts = preferentialVo.getOrderDiscounts();
//        if (!CollectionUtils.isEmpty(orderDiscounts)) {
//            orderDiscounts.forEach(item -> item.setOrderId(orders.getId()));
//            orderDiscountService.saveBatch(orderDiscounts);
//        }
//
//        //外卖订单插入订单地址表
//        OrderAddress orderAddress = orderAddressService.createByCustomerAddressId(context.request.getCustomerAddressId());
//        orderAddress.setOrderId(orders.getId());
//        Customer customer = customerService.getById(orders.getCustomerId());
//        orderAddress.setSex(customer.getSex());
//        orderAddressService.save(orderAddress);
//
//    }
//
//    @Override
//    public BigDecimal shippingFeeRule() {
//        if (!Environment.isProd()) {
//            return new BigDecimal("4");
//        }
//        if (context.shop.getDeliveryPlatform().equals(DeliveryPlatformEnum.ZPS.getCode())) {
//            //todo 商家没有对接外卖平台，自配送
//            return BigDecimal.ZERO;
//        }
//
//        BigDecimal shippingFee = shippingFeeRuleService.getByDistance(context.distance);
//        if (shippingFee == null) {
//            throw new PinetException("配送费查询失败");
//        }
//        context.shippingFee = shippingFee;
//        return shippingFee;
//    }
//
//
//
//
//    /**
//     * 获取平台配送费
//     *
//     * @return BigDecimal
//     */
//    private BigDecimal getShippingFeePlat(BigDecimal orderProdPrice) {
//        if (Objects.equals(context.request.getOrderType(),OrderTypeEnum.SELF_PICKUP.getCode())) {
//            return BigDecimal.ZERO;
//        }
//        //测试环境默认4元吧
//        if (!Environment.isProd()) {
//            return new BigDecimal("4");
//        }
//
//        if (StringUtil.isBlank(context.shop.getDeliveryShopNo())
//                || context.shop.getDeliveryPlatform().equals(DeliveryPlatformEnum.ZPS.getCode())) {
//            //todo 商家没有对接外卖平台，自配送
//            return BigDecimal.ZERO;
//        }
//        //查询收货地址
//        CustomerAddress customerAddress = customerAddressService.getById(context.request.getCustomerAddressId());
//        if (ObjectUtil.isNull(customerAddress)) {
//            throw new PinetException("收货地址异常");
//        }
//
//        Snowflake snowflake = IdUtil.getSnowflake();
//
//        AddOrderReq addOrderReq = AddOrderReq.builder()
//                .shopNo(context.shop.getDeliveryShopNo())
//                .originId(snowflake.nextIdStr())
//                .cargoPrice(orderProdPrice.doubleValue())
//                .prepay(0)
//                .receiverName(customerAddress.getName())
//                .receiverAddress(customerAddress.getAddress())
//                .receiverPhone(customerAddress.getPhone())
//                .callback("http://xinjiangapi.ypxlbz.com/house/qingshi/api/dada/deliverFee/callback")
//                .cargoWeight(0.5)
//                .receiverLat(customerAddress.getLat().doubleValue())
//                .receiverLng(customerAddress.getLng().doubleValue())
//                .build();
//        try {
//            AddOrderResp addOrderResp = daDaService.queryDeliverFee(addOrderReq);
//            return BigDecimal.valueOf(addOrderResp.getDeliverFee());
//        } catch (RpcException e) {
//            throw new PinetException("查询配送费服务失败");
//        }
//    }
//
//    /**
//     * 是否满足计算佣金的条件
//     *
//     * @param customerId 下单人 ID
//     * @param shareId 分享人 ID
//     */
//    private boolean commissionCondition(Long customerId,Long shareId) {
//        if (ObjectUtil.isNull(shareId) || shareId <= 0) {
//            return false;
//        }
//
//        //判断下单人和分享人是否是同一个人
//        if (shareId.equals(customerId)) {
//            return false;
//        }
//        //分享人会员等级
//        Integer shareMemberLevel = customerMemberService.getMemberLevel(shareId);
//
//        //邀请人必须是店帮主  被邀人不能是店帮主
//        return shareMemberLevel.equals(MemberLevelEnum._20.getCode()) && !context.orderUserLevel.equals(MemberLevelEnum._20.getCode());
//    }
//
//}
