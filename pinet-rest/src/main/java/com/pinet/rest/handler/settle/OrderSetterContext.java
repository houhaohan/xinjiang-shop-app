package com.pinet.rest.handler.settle;

import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.enums.SettlementTypeEnum;
import com.pinet.rest.service.*;
import java.util.List;
import java.util.Objects;


/**
 * @description: 订单结算上下文
 * @author: chengshuanghui
 * @date: 2024-03-22 17:55
 */
public class OrderSetterContext {
    protected ICartService cartService;
    protected DishSettleContext dishSettleContext;
    protected Long userId;
    protected List<OrderProduct> response;
    protected OrderSettleHandler orderSettleHandler;


    public void setUserId(Long userId){
        this.userId = userId;
    }

    public void setCartService(ICartService cartService){
        this.cartService = cartService;
    }

    public void setDishSettleContext(DishSettleContext dishSettleContext){
        this.dishSettleContext = dishSettleContext;
    }


    public List<OrderProduct> getResponse(){
        return this.response;
    }


    public void execute(){
        if(Objects.equals(dishSettleContext.request.getSettlementType(), SettlementTypeEnum.CART_BUY.getCode())){
            this.orderSettleHandler = new CartOrderSettleHandler(this);
        }else {
            this.orderSettleHandler = new DirectBuyOrderSettleHandler(this);
        }
        orderSettleHandler.handler();
    }
}
