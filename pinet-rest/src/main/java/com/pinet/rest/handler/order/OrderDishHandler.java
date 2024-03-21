package com.pinet.rest.handler.order;

import com.pinet.rest.entity.OrderProduct;

public interface OrderDishHandler {

    public OrderProduct execute(DirectOrderRequest request);


    public OrderProduct execute(CartOrderProductRequest request);
}
