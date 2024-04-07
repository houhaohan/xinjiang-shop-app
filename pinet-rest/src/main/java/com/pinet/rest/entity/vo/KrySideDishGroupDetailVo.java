package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 菜品小料
 * @author: chengshuanghui
 * @date: 2024-04-07 17:08
 */
@Data
@ApiModel(description = "小料明细")
public class KrySideDishGroupDetailVo {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("小料分组ID")
    private Long sideDishGroupId;

    @ApiModelProperty("小料ID")
    private String sideDishId;

    @ApiModelProperty("小料名称")
    private String sideDishName;

    @ApiModelProperty("加价")
    private BigDecimal addPrice;
}
