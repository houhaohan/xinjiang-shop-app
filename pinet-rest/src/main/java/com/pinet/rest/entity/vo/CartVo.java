package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "CartVo",description = "购物车")
public class CartVo {

    @ApiModelProperty("商品数量")
    private Integer prodNum;

    @ApiModelProperty("商品价格")
    private BigDecimal price;
}
