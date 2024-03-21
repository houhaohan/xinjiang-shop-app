package com.pinet.rest.handler.order;

import com.pinet.rest.entity.Cart;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.dto.CreateOrderDto;
import com.pinet.rest.entity.vo.CreateOrderVo;
import lombok.Data;

import java.util.List;

@Data
public class OrderContext {
    /**
     * 下单人用户 ID
     */
    protected Long customerId;

    /**
     * 下单人用户层级
     */
    protected Integer orderUserlevel;


    protected Shop shop;


    protected List<Cart> cartList;

    /**
     * 距离
     */
    protected Double distance;


    protected CreateOrderDto request;

    protected CreateOrderVo response;



}
