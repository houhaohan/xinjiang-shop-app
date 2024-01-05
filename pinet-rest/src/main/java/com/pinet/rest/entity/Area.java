package com.pinet.rest.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 地址区域表
 * </p>
 *
 * @author wlbz
 * @since 2024-01-04
 */
@Getter
@Setter
@ApiModel(value = "Area对象", description = "地址区域表")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Area extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("父级")
    private Integer parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("简称")
    private String shortName;

    @ApiModelProperty("经度")
    private String longitude;

    @ApiModelProperty("纬度")
    private String latitude;

    @ApiModelProperty("级别 1 省 2 市 3 区 4 乡镇/街道")
    private Integer level;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("状态 0 无效 1 有效")
    private Integer status;

    @ApiModelProperty("字母")
    private String letter;


}
