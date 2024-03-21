//package com.pinet.rest.handler.order;
//
//import com.pinet.core.exception.PinetException;
//import com.pinet.keruyun.openapi.constants.DishType;
//import com.pinet.rest.entity.Cart;
//import com.pinet.rest.entity.CartProductSpec;
//import com.pinet.rest.entity.OrderProduct;
//import com.pinet.rest.entity.bo.QueryOrderProductBo;
//import com.pinet.rest.entity.enums.CartStatusEnum;
//import com.pinet.rest.entity.vo.OrderProductVo;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * @description: 订单结算处理器
// * @author: chengshuanghui
// * @date: 2024-03-21 13:59
// */
//public class OrderSettlementHandler {
//
//    /**
//     * 购物车结算
//     * @param cartList
//     */
//    public void handler(List<Cart> cartList,Integer orderType){
//        OrderProductVo orderProductVo = new OrderProductVo();
//        for(Cart cart : cartList){
//            if (Objects.equals(cart.getCartStatus(), CartStatusEnum.EXPIRE.getCode())) {
//                throw new PinetException("购物车内有失效的商品,请删除后在结算");
//            }
//            if (DishType.COMBO.equalsIgnoreCase(cart.getDishType())) {
//                OrderProductVo comboOrderItemInfo = getComboOrderItemInfo(cart, orderType);
//                orderProducts.add(comboOrderItemInfo);
//            } else {
//                List<CartProductSpec> cartProductSpecs = cartProductSpecService.getByCartId(cart.getId());
//                List<Long> shopProdSpecIds = cartProductSpecs.stream().map(CartProductSpec::getShopProdSpecId).collect(Collectors.toList());
//                QueryOrderProductBo queryOrderProductBo = new QueryOrderProductBo(cart.getShopProdId(), cart.getProdNum(), shopProdSpecIds, orderType);
//                OrderProduct orderProduct = this.getByQueryOrderProductBo(queryOrderProductBo);
//                orderProducts.add(orderProduct);
//            }
//        }
//
//    }
//
//}
