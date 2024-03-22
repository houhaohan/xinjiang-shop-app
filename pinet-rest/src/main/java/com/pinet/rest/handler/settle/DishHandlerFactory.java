package com.pinet.rest.handler.settle;

import com.pinet.keruyun.openapi.constants.DishType;

import java.util.Objects;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-03-22 17:38
 */
public class DishHandlerFactory {
    private DishSettleHandler dishSettleHandler;
    private String dishType;

    public DishHandlerFactory(String dishType){
        this.dishType = dishType;
        if(Objects.equals(dishType, DishType.SINGLE)){
            this.dishSettleHandler = new SingleDishHandler();
        }else {
            this.dishSettleHandler = new ComboDishHandler();
        }
    }

    public void execute(){
        dishSettleHandler.handler();
    }
}
