package com.pinet.rest.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductRequest {

    private Long orderId;

    /**
     * 商品 ID
     */
    private Long shopProdId;

    /**
     * 客如云商品 ID
     */
    private String dishId;

    /**
     * 商品名称
     */
    private String prodName;

    /**
     * 商品单位
     */
    private String unit;

    /**
     * 商品图片
     */
    private String prodImg;

    /**
     * 商品购买数量
     */
    private Integer prodNum;

    private boolean calculate;

    /**
     * 订单状态  1外卖  2自提
     */
    private Integer orderType;

    /**
     * 用户ID
     */
    private Long customerId;




}
