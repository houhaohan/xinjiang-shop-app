package com.pinet.rest.handler.order;

import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderDishContext {

    protected final IOrderComboDishService orderComboDishService;
    protected final IOrderComboDishSpecService orderComboDishSpecService;
    protected final ICartComboDishService cartComboDishService;
    protected final ICartComboDishSpecService cartComboDishSpecService;
    protected final IOrderProductService orderProductService;
    protected final IKryComboGroupDetailService kryComboGroupDetailService;
    protected final IShopProductService shopProductService;
    protected final IShopProductSpecService shopProductSpecService;
    protected final IOrderProductSpecService orderProductSpecService;
    protected final ICartProductSpecService cartProductSpecService;

    public OrderDishHandler handler(String dishType){
        if(Objects.equals(dishType, DishType.SINGLE)){
            return  new OrderSingleDishHandler(this);
        }else {
            return new OrderComboDishHandler(this);
        }
    }

}
