package com.pinet.rest.handler.settle;

import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.enums.SettlementTypeEnum;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.service.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;


/**
 * @description: 订单结算上下文
 * @author: chengshuanghui
 * @date: 2024-03-22 17:55
 */
public class OrderSetterContext {
    protected ICartService cartService;
    protected IVipUserService vipUserService;
    protected OrdersMapper ordersMapper;
    protected DishSettleContext dishSettleContext;
    protected Long userId;
    protected Double distance;
    protected String deliveryPlatform;
    protected List<OrderProduct> response;

    protected BigDecimal packageFee = BigDecimal.ZERO;
    protected BigDecimal orderProdPrice = BigDecimal.ZERO;
    protected Integer orderProductNum = 0;
    protected BigDecimal shippingFee = BigDecimal.ZERO;


    protected OrderSettleHandler orderSettleHandler;


    public void setUserId(Long userId){
        this.userId = userId;
    }

    public void setCartService(ICartService cartService){
        this.cartService = cartService;
    }

    public void setVipUserService(IVipUserService vipUserService){
        this.vipUserService = vipUserService;
    }
    public void setOrdersMapper(OrdersMapper ordersMapper){
        this.ordersMapper = ordersMapper;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setDeliveryPlatform(String deliveryPlatform) {
        this.deliveryPlatform = deliveryPlatform;
    }

    public void setDishSettleContext(DishSettleContext dishSettleContext){
        this.dishSettleContext = dishSettleContext;
    }


    public List<OrderProduct> getResponse(){
        return this.response;
    }

    public BigDecimal getPackageFee(){
        return this.packageFee;
    }

    public BigDecimal getOrderProdPrice(){
        return this.orderProdPrice;
    }

    public Integer getOrderProductNum(){
        return this.orderProductNum;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
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
