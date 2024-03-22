package com.pinet.rest.handler.settle;

import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.dto.OrderSettlementDto;
import com.pinet.rest.entity.enums.CartStatusEnum;
import com.pinet.rest.service.ICartService;
import com.pinet.rest.service.IOrderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @description: 购物车订单结算
 * @author: chengshuanghui
 * @date: 2024-03-22 16:35
 */
public class CartOrderSettleHandler extends OrderSettleAbstractHandler{

    public CartOrderSettleHandler(OrderSetterContext context){
        this.context = context;
    }

    public void handler(){
        List<OrderProduct> orderProducts = new ArrayList<>();
        List<Cart> cartList = context.cartService.getByUserIdAndShopId(context.userId, context.dishSettleContext.request.getShopId());
        if (CollectionUtils.isEmpty(cartList)) {
            throw new PinetException("购物车内没有需要结算的商品");
        }
        cartList.forEach(cart -> {
            if (Objects.equals(cart.getCartStatus(), CartStatusEnum.EXPIRE.getCode())) {
                throw new PinetException("购物车内有失效的商品,请删除后在结算");
            }
            context.dishSettleContext
                    .execute(cart.getDishType())
                    .handler(cart);
            OrderProduct orderProduct = context.dishSettleContext.response;
            orderProducts.add(orderProduct);

            context.packageFee = BigDecimalUtil.sum(context.packageFee,orderProduct.getPackageFee());
            context.orderProdPrice = BigDecimalUtil.sum(context.orderProdPrice,orderProduct.getProdPrice());
            context.orderProductNum = context.orderProductNum + orderProduct.getProdNum();
        });
        context.response = orderProducts;
    }


}
