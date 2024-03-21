package com.pinet.rest.handler.order;

import lombok.Data;


@Data
public class CartOrderProductRequest extends OrderProductRequest{

    /**
     * 购物车 ID
     */
    private Long cartId;

}
