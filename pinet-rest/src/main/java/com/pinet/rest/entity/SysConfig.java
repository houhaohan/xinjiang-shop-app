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
 * 
 * </p>
 *
 * @author wlbz
 * @since 2024-06-19
 */
@Getter
@Setter
@TableName("sys_config")
@ApiModel(value = "SysConfig对象", description = "")
public class SysConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("配置描述")
    private String name;

    @ApiModelProperty("配置详情名称")
    private String code;

    @ApiModelProperty("值")
    private String val;

    @ApiModelProperty("配置状态")
    private Integer state;


}
