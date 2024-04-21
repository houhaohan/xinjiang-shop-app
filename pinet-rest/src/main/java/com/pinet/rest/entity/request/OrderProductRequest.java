package com.pinet.rest.entity.request;

import com.pinet.rest.entity.dto.SideDishGroupDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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

    /**
     * 是否满足计算佣金的条件
     */
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
