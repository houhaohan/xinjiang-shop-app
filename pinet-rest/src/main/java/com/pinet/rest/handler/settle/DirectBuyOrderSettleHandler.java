package com.pinet.rest.handler.settle;


import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.dto.OrderSettlementDto;


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

    public void handler(){
        List<OrderProduct> orderProducts = new ArrayList<>();
        DishSettleContext dishSettleContext = context.dishSettleContext;
        ShopProduct shopProduct = dishSettleContext.shopProductService.getById(dishSettleContext.request.getShopProdId());
        context.dishSettleContext
                .execute(shopProduct.getDishType())
                .handler();
        orderProducts.add(context.dishSettleContext.response);
        context.response = orderProducts;
    }
}
