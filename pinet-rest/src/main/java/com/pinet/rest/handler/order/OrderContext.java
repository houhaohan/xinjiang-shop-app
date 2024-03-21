package com.pinet.rest.handler.order;

import com.pinet.common.mq.util.JmsUtil;
import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.Cart;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.dto.CreateOrderDto;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.entity.enums.SettlementTypeEnum;
import com.pinet.rest.entity.vo.CreateOrderVo;
import com.pinet.rest.handler.ComboDishCartHandler;
import com.pinet.rest.handler.SingleDishCartHandler;
import com.pinet.rest.mapper.OrdersMapper;
import com.pinet.rest.service.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderContext {

    protected final OrderPreferentialManager orderPreferentialManager;
    protected final IShippingFeeRuleService shippingFeeRuleService;
    protected final ICustomerMemberService customerMemberService;
    protected final OrdersMapper ordersMapper;
    protected final IShopProductService shopProductService;
    protected final IOrderAddressService orderAddressService;
    protected final IOrderDiscountService orderDiscountService;
    protected final ICustomerService customerService;
    protected final IDaDaService daDaService;
    protected final ICustomerAddressService customerAddressService;
    protected final ICartService cartService;
    protected final OrderDishContext orderDishContext;
    protected final JmsUtil jmsUtil;
    protected Long customerId;
    protected Double distance;
    protected Shop shop;
    protected List<Cart> cartList;
    protected CreateOrderDto request;
    protected CreateOrderVo response;
    protected OrderHandler orderHandler;

    public void setCustomerId(Long customerId){
        this.customerId =  customerId;
    }

    public void setDistance(Double distance){
        this.distance =  distance;
    }

    public void setShop(Shop shop){
        this.shop = shop;
    }

    public void setCartList(List<Cart> cartList){
        this.cartList = cartList;
    }

    public void setRequest(CreateOrderDto request){
        this.request = request;
    }

    public CreateOrderVo getResponse(){
        return this.response;
    }

    /**
     * 订单处理方法
     */
    public void handler(){
        if(Objects.equals(request.getSettlementType(), SettlementTypeEnum.CART_BUY.getCode())){
            orderHandler = new CartOrderHandler(this);
        }else {
            orderHandler = new DirectBuyOrderHandler(this);
        }
        orderHandler.create();
    }



}
