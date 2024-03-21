package com.pinet.rest.handler.order;

import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.OrderProduct;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class OrderDishAbstractHandler {

    protected OrderProduct buildOrderProduct(OrderProductRequest request, BigDecimal unitPrice){
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderId(request.getOrderId());
        orderProduct.setShopProdId(request.getShopProdId());
        orderProduct.setDishId(request.getDishId());
        orderProduct.setProdUnitPrice(unitPrice);
        orderProduct.setProdNum(request.getProdNum());
        orderProduct.setProdPrice(BigDecimalUtil.multiply(orderProduct.getProdUnitPrice(),orderProduct.getProdNum(), RoundingMode.HALF_UP));
        orderProduct.setProdName(request.getProdName());
        orderProduct.setUnit(request.getUnit());
        orderProduct.setProdImg(request.getProdImg());
        if(request.isCalculate()){
            orderProduct.setCommission(BigDecimalUtil.multiply(orderProduct.getProdPrice(),0.1));
        }

        return orderProduct;
    }

}
