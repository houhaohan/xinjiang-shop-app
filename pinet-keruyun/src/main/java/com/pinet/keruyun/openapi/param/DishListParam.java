package com.pinet.keruyun.openapi.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DishListParam extends Page {
    @ApiModelProperty("分类ID")
    private List<String> categoryIds;
    @ApiModelProperty("菜品类型， SINGLE：单菜 ，COMBO：套餐， SIDE：配料")
    private List<String> dishTypes;
    @ApiModelProperty("菜品ID")
    private List<String> dishIds;
}
