package com.pinet.rest.handler.order;

import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.*;
import com.pinet.rest.entity.enums.*;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CartOrderHandler extends OrderAbstractHandler {

    public CartOrderHandler(OrderContext context){
        this.context = context;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create() {
        Orders orders = buildOrder();
        boolean condition = commissionCondition(orders.getCustomerId(),orders.getShareId());
        List<OrderProduct> orderProducts = new ArrayList<>();
        for(Cart cart : context.cartList){
            ShopProduct shopProduct = context.shopProductService.getById(cart.getShopProdId());
            //判断店铺商品是否下架
            if (Objects.equals(shopProduct.getShopProdStatus(), ShopProdStatusEnum.OFF_SHELF.getCode())) {
                throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
            }

            //判断店铺商品是否删除
            if (shopProduct.getDelFlag() == 1) {
                throw new PinetException(shopProduct.getProductName() + "已下架,请重新选择");
            }
            //订单类型( 1外卖  2自提)
            CartOrderProductRequest request = new CartOrderProductRequest();
            request.setCartId(cart.getId());
            request.setDishId(cart.getDishId());
            request.setOrderId(orders.getId());
            request.setShopProdId(cart.getShopProdId());
            request.setProdName(shopProduct.getProductName());
            request.setProdNum(cart.getProdNum());
            request.setProdImg(shopProduct.getProductImg());
            request.setUnit(shopProduct.getUnit());
            request.setOrderType(context.request.getOrderType());
            request.setCalculate(condition);

            OrderProduct orderProduct = context.orderDishContext
                    .handler(shopProduct.getDishType())
                    .execute(request);
            orders.setPackageFee(BigDecimalUtil.sum(orders.getPackageFee(),orderProduct.getPackageFee()));
            orders.setOrderProdPrice(BigDecimalUtil.sum(orders.getOrderProdPrice(),orderProduct.getProdPrice()));
            orderProducts.add(orderProduct);
        }

        afterHandler(orders,orderProducts);

        // 清除购物车
        context.cartService.delCartByShopId(orders.getShopId(),orders.getCustomerId());
    }
}
