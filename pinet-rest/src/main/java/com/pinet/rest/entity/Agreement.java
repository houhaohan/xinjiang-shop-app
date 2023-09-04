package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 协议表
 * </p>
 *
 * @author wlbz
 * @since 2023-09-04
 */
@Getter
@Setter
@ApiModel(value = "Agreement对象", description = "协议表")
public class Agreement{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("协议编号")
    private String code;

    @ApiModelProperty("协议标题")
    private String title;

    @ApiModelProperty("协议内容")
    private String content;

    @ApiModelProperty("创建人id")
    private Integer adminId;

    @ApiModelProperty("创建人名")
    private String adminName;


}
