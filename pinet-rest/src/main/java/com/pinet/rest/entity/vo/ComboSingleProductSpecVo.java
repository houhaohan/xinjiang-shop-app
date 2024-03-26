package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-03-26 14:37
 */
@Data
public class ComboSingleProductSpecVo {

    @ApiModelProperty("商品样式ID")
    private Long shopProdSpecId;

    @ApiModelProperty("商品样式名称")
    private String shopProdSpecName;

    @ApiModelProperty("加价（单位分）")
    private Long addPrice;
}
