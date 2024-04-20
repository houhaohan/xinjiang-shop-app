package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-04-07 17:12
 */
@Data
@ApiModel(description = "小料分组")
public class KrySideDishGroupVo {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("菜品ID")
    private String dishId;

    @ApiModelProperty("分组名称")
    private String groupName;

    @ApiModelProperty("小料明细")
    private List<KrySideDishGroupDetailVo> sideDishGroupDetailList;

}
