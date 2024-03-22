package com.pinet.rest.handler.settle;

import com.pinet.keruyun.openapi.constants.DishType;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.dto.OrderSettlementDto;
import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class DishSettleContext {

    protected final ICartProductSpecService cartProductSpecService;
    protected final IShopProductService shopProductService;
    protected final IShopProductSpecService shopProductSpecService;
    protected final IProductSkuService productSkuService;
    protected final IKryComboGroupDetailService kryComboGroupDetailService;
    protected final ICartComboDishService cartComboDishService;
    protected final ICartComboDishSpecService cartComboDishSpecService;
    protected final IKryComboGroupService kryComboGroupService;

    protected OrderSettlementDto request;
    protected OrderProduct response;


    public DishSettleHandler execute(String dishType){
        if(Objects.equals(dishType, DishType.SINGLE)){
            return new SingleDishHandler(this);
        }else {
            return new ComboDishHandler(this);
        }
    }

    public void setRequest(OrderSettlementDto request){
        this.request = request;
    }
}
