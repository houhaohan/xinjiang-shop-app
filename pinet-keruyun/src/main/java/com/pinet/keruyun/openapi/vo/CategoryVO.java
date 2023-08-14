package com.pinet.keruyun.openapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class CategoryVO {
    @ApiModelProperty("分类ID")
    private String categoryId;
    @ApiModelProperty("父级分类ID")
    private String parentId;
    @ApiModelProperty("分类名称")
    private String categoryName;
    @ApiModelProperty("排序值")
    private Integer sort;
    @ApiModelProperty("分类类型，DISH：菜品分类 SIDE_DISH_GROUP:加料分组")
    private String categoryType;
}
