package com.pinet.keruyun.openapi.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CategoryParam {
    @ApiModelProperty("分类类型，DISH：菜品分类 SIDE_DISH_GROUP:加料分组")
    private List<String> categoryTypes;
}
