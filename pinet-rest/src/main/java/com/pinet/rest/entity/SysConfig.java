package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author wlbz
 * @since 2024-06-19
 */
@Getter
@Setter
@TableName("sys_config")
@ApiModel(value = "SysConfig对象", description = "系统配置表")
public class SysConfig {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("配置描述")
    private String name;

    @ApiModelProperty("code编码")
    private String code;

    @ApiModelProperty("值")
    private String val;

    @ApiModelProperty("配置状态")
    private Integer state;


}
