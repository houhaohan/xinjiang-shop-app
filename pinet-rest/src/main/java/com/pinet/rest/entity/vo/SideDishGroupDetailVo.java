package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("菜品加料信息")
public class SideDishGroupDetailVo {
    @ApiModelProperty("加料id")
    private Long id;

    @ApiModelProperty("单品 ID")
    private Long shopProdId;

    @ApiModelProperty("加料id")
    private String sideDishId;

    @ApiModelProperty("加料名称")
    private String sideDishName;

    @ApiModelProperty("加料分组ID")
    private Long sideDishGroupId;

    @ApiModelProperty("加价")
    private BigDecimal addPrice;
}
