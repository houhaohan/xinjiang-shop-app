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
 * 菜品加料组明细
 * </p>
 *
 * @author wlbz
 * @since 2024-04-01
 */
@Getter
@Setter
@TableName("kry_side_dish_group_detail")
@ApiModel(value = "KrySideDishGroupDetail对象", description = "菜品加料组明细")
public class KrySideDishGroupDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品 ID")
    private Long shopProdId;

    @ApiModelProperty("菜品ID")
    private String dishId;

    @ApiModelProperty("分组ID")
    private String sideDishGroupId;

    @ApiModelProperty("加料id")
    private String sideDishId;

    @ApiModelProperty("加料名称")
    private String sideDishName;

    @ApiModelProperty("配料价格 单位： 分")
    private Long addPrice;

    @ApiModelProperty("排序值")
    private Integer sort;


}
