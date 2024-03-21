package com.pinet.rest.handler.order;//package com.pinet.rest.handler.orders;
//
//
//import cn.hutool.core.util.ObjectUtil;
//import com.pinet.core.exception.PinetException;
//import com.pinet.core.util.BigDecimalUtil;
//import com.pinet.keruyun.openapi.constants.DishType;
//import com.pinet.rest.entity.Cart;
//import com.pinet.rest.entity.OrderProduct;
//import com.pinet.rest.entity.Orders;
//import com.pinet.rest.entity.ShopProduct;
//import com.pinet.rest.entity.enums.MemberLevelEnum;
//import com.pinet.rest.entity.enums.OrderSourceEnum;
//import com.pinet.rest.entity.vo.PreferentialVo;
//import com.pinet.rest.service.ICustomerMemberService;
//import com.pinet.rest.service.IOrdersService;
//import com.pinet.rest.service.IShopProductService;
//import com.pinet.rest.service.OrderPreferentialManager;
//import com.pinet.rest.strategy.MemberLevelStrategyContext;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
///**
// * @description: 自提单
// * @author: chengshuanghui
// * @date: 2024-03-09 10:23
// */
//@Component
//@RequiredArgsConstructor
//public class SelfPickupOrderHandler extends OrderAbstractHandler{
//    private final ICustomerMemberService customerMemberService;
//    private final IOrdersService ordersService;
//    private final IShopProductService shopProductService;
//    private final OrderSingleDishHandler orderSingleDishHandler;
//    private final OrderComboDishHandler orderComboDishHandler;
//    private final OrderPreferentialManager orderPreferentialManager;
//
//
//    @Override
//    public void create() {
//        Orders orders = buildOrder();
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
//    }
//
//    @Override
//    public BigDecimal shippingFeeRule() {
//        return BigDecimal.ZERO;
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
//}
