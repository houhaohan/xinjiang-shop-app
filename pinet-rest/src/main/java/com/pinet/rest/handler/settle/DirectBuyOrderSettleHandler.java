package com.pinet.rest.handler.settle;


import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.ShopProduct;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 直接购买订单结算
 * @author: chengshuanghui
 * @date: 2024-03-22 16:36
 */
public class DirectBuyOrderSettleHandler extends OrderSettleAbstractHandler {

    public DirectBuyOrderSettleHandler(OrderSetterContext context){
        this.context = context;
    }

    @Override
    public void handler(){
        List<OrderProduct> orderProducts = new ArrayList<>();
        DishSettleContext dishSettleContext = context.dishSettleContext;
        ShopProduct shopProduct = dishSettleContext.shopProductService.getById(dishSettleContext.request.getShopProdId());
        dishSettleContext.execute(shopProduct.getDishType()).handler();
        OrderProduct orderProduct = dishSettleContext.response;
        orderProducts.add(orderProduct);
        context.response = orderProducts;
        context.packageFee = orderProduct.getPackageFee();
        context.orderProdPrice = orderProduct.getProdPrice();
        context.orderProductNum = orderProduct.getProdNum();
        context.shippingFee = calculate(context.dishSettleContext.request.getOrderType(), context.distance.intValue(), context.deliveryPlatform);

    }
}
