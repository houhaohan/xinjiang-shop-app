package com.pinet.rest.handler.order;

import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.request.OrderProductRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @description 订单菜品抽象处理器
 * @author chengshuanghui
 * @data 2024-03-21 15:00
 */
public abstract class OrderDishAbstractHandler implements OrderDishHandler {
    protected OrderDishContext context;

    protected OrderProduct build(OrderProductRequest request, BigDecimal unitPrice,BigDecimal sidePrice){
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderId(request.getOrderId());
        orderProduct.setShopProdId(request.getShopProdId());
        orderProduct.setDishId(request.getDishId());
        orderProduct.setProdUnitPrice(unitPrice);
        orderProduct.setSidePrice(sidePrice);
        orderProduct.setProdNum(request.getProdNum());
        BigDecimal prodPrice = BigDecimalUtil.multiply(BigDecimalUtil.sum(unitPrice, sidePrice), orderProduct.getProdNum(), RoundingMode.HALF_UP);
        orderProduct.setProdPrice(prodPrice);
        orderProduct.setProdName(request.getProdName());
        orderProduct.setUnit(request.getUnit());
        orderProduct.setProdImg(request.getProdImg());
        if(request.isCalculate()){
            orderProduct.setCommission(BigDecimalUtil.multiply(orderProduct.getProdPrice(),0.1));
        }
        return orderProduct;
    }

}
