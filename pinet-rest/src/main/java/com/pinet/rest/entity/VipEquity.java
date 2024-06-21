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
 * VIP权益表
 * </p>
 *
 * @author wlbz
 * @since 2024-06-11
 */
@Getter
@Setter
@TableName("vip_equity")
@ApiModel(value = "VipEquity对象", description = "VIP权益表")
public class VipEquity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("VIP ID")
    private Long vipId;

    @ApiModelProperty("VIP等级")
    private Long vipLevel;

    @ApiModelProperty("VIP名称")
    private String vipName;

    @ApiModelProperty("权益内容")
    private String content;

    @ApiModelProperty("图标")
    private String icon;


}
