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
 * 客如云门套餐分组表
 * </p>
 *
 * @author wlbz
 * @since 2023-08-29
 */
@Getter
@Setter
@TableName("kry_combo_group")
@ApiModel(value = "KryComboGroup对象", description = "客如云门套餐分组表")
public class KryComboGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("shop_prod 表主键ID")
    private Long shopProdId;

    @ApiModelProperty("菜品ID")
    private String dishId;

    @ApiModelProperty("分组名称")
    private String groupName;

    @ApiModelProperty("客如云SKU ID")
    private String krySkuId;

    @ApiModelProperty("套餐分组子菜的最大选择数")
    private Integer maxChoose;

    @ApiModelProperty("套餐分组子菜的最小选择数")
    private Integer minChoose;

    @ApiModelProperty("排序值")
    private Integer sort;

    @ApiModelProperty("是否可重复选 Y:是；N:否")
    private String repeatable;

    @ApiModelProperty("套餐组类型（FIXED:固定，OPTIONAL:可选）")
    private String groupType;


}
