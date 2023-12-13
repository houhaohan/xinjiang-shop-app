package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 客如云门套餐分组明细表
 * </p>
 *
 * @author wlbz
 * @since 2023-08-29
 */
@Getter
@Setter
@TableName("kry_combo_group_detail")
@ApiModel(value = "KryComboGroupDetail对象", description = "客如云门套餐分组明细表")
public class KryComboGroupDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("菜品ID")
    private String dishId;

    @ApiModelProperty("套餐分组ID")
    private Long comboGroupId;

    @ApiModelProperty("子菜菜品ID")
    private String singleDishId;

    @ApiModelProperty("最大可选数量")
    private Integer maxChoose;

    @ApiModelProperty("最小可选数量")
    private Integer minChoose;

    @ApiModelProperty("固定数量")
    private Integer fixChoose;

    @ApiModelProperty("子菜SKU ID")
    private String dishSkuId;

    @ApiModelProperty("套餐分组为可选分组时的子菜加价金额,单位：分")
    private Long dishSkuPrice;

    @ApiModelProperty("可选类型(OPTIONAL-可选/REQUIRED-必选)")
    private String optType;

    @ApiModelProperty("是否为默认子菜")
    private String defaultFlag;

    @ApiModelProperty("子菜名称")
    private String dishName;

    @ApiModelProperty("子菜规格名")
    private String specName;

    @ApiModelProperty("子菜售卖价，单位：分")
    private Long sellPrice;

    @ApiModelProperty("小程序实际售价，单位：分")
    private Long price;

    @ApiModelProperty("子菜是否为多规格 Y:是，N:否")
    private String multiSpecFlag;

    @ApiModelProperty("排序号")
    private Long sort;


}
