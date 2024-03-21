package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description: 订单套餐明细
 * @author: chengshuanghui
 * @date: 2024-03-20 09:55
 */
@Data
public class OrderComboDishDto {

    @ApiModelProperty("套餐ID")
    private Long shopProdId;

    @ApiModelProperty("单品ID")
    private Long singleProdId;

    @ApiModelProperty("单品数量")
    private Integer quantity;

    @ApiModelProperty("套餐内单品样式")
    private List<OrderComboDishSpecDto> orderComboDishSpecList;
}
