package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-06-12 09:49
 */
@Data
public class VipEquityVO {
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("VIP ID")
    private Long vipId;

    @ApiModelProperty("VIP等级")
    private Long vipLevel;

    @ApiModelProperty("VIP名称")
    private String vipName;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("icon")
    private String icon;

    @ApiModelProperty("是否拥有权益，0-否，1-是")
    private Integer have;
}
