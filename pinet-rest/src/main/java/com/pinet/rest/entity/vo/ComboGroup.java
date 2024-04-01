package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("套餐组")
public class ComboGroup {

    @ApiModelProperty(value = "套餐组ID")
    private Long groupId;

    @ApiModelProperty(value = "套餐组名称")
    private String groupName;

    @ApiModelProperty(value = "套餐组名称")
    private String groupType;

    @ApiModelProperty(value = "最大可选数量")
    private Integer maxChoose;

    @ApiModelProperty(value = "最小可选数量")
    private Integer minChoose;

    @ApiModelProperty(value = "子菜列表")
    private List<ShopProductVo> singleDishList = new ArrayList<>();
}
