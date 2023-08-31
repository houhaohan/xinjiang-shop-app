package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DinnerDishProduct {

    @ApiModelProperty("客如云菜品ID")
    private Long id;

    @ApiModelProperty("合作方菜品ID（第三方自定义）")
    private String tpId;

    @ApiModelProperty("套餐的子菜或单菜加料的父级uuid（即开发者自定义的订单中菜品唯一标识），如果是套餐子菜或单菜加料才有此值，单菜此字段不传")
    private String parentUuid;

    @ApiModelProperty("菜品类型 : 菜品种类 0:单菜 1:套餐 2:加料。不填默认唯0，套餐子菜请填0")
    private Integer type;

    @ApiModelProperty("菜品名称")
    private String name;

    @ApiModelProperty("菜品单位")
    private String unit;

    @ApiModelProperty("商品信息")
    private List<ProductProperty> properties;

    @ApiModelProperty("菜品价格 单位：分")
    private Integer price;

    @ApiModelProperty("份数")
    private Integer quantity;

    @ApiModelProperty("菜品总价==(菜品单价 + 变价) * 菜品数量，单位：分")
    private Integer totalFee;

    @ApiModelProperty("菜品备注")
    private String remark;
}
