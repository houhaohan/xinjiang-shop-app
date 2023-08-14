package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@ToString
@NoArgsConstructor
public class KryOrderProduct implements Serializable {

    private static final long serialVersionUID = 5652599704647519477L;
    @ApiModelProperty(value = "合作方菜品ID", required = true)
    @NotNull
    private String tpId;

    @ApiModelProperty(value = "客如云菜品ID")
    private Long id;

    @ApiModelProperty("套餐主菜Id")
    private Long parentId;

    @ApiModelProperty(value = "订单菜品记录uuid")
    private String uuid;

    @ApiModelProperty(value = "订单父级菜品记录uuid（子菜使用）")
    private String parentUuid;

    @ApiModelProperty(value = "菜品类型 : 菜品种类 0:单菜 1:套餐 2:加料。不填默认唯0，套餐子菜请填0")
    private Integer type;

    @ApiModelProperty("套餐分组Id")
    private Long dishSetmealGroupId;

    @ApiModelProperty(value = "菜品名称", required = true)
    @NotNull
    @NotBlank
    private String name;

    @ApiModelProperty(value = "菜品单位")
    private String unit;

    @ApiModelProperty(value = "菜品属性", required = true)
    @Valid
    private List<ProductProperty> properties;

    @ApiModelProperty(value = "菜品价格 单位：分", required = true)
    @NotNull
    @Min(0)
    private Integer price;

    @ApiModelProperty(value = "份数", required = true)
    @NotNull
    @Min(0)
    private Integer quantity;

    @ApiModelProperty(value = "菜品总价==(菜品单价 + 变价) * 菜品数量，单位：分", required = true)
    @Min(0)
    @NotNull
    private Integer totalFee;

    @ApiModelProperty("菜品备注")
    private String remark;

    @ApiModelProperty("餐盒单价，单位：分")
    @NotNull
    @Min(0)
    private Integer packagePrice;    //餐盒单价，单位：分

    @ApiModelProperty("餐盒数量")
    @NotNull
    @Min(0)
    private Integer packageQuantity;//餐盒数量

}



