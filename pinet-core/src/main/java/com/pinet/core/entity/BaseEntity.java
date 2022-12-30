package com.pinet.core.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "创建人")
    @TableField(exist = false)
    private String createUser;
    @ApiModelProperty(value = "创建人ID")
    @TableField(value = "create_by",fill = FieldFill.INSERT)
    private Long createBy;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
    @ApiModelProperty(value = "更新人")
    @TableField(exist = false)
    private String updateUser;
    @ApiModelProperty(value = "更新人ID")
    @TableField(value = "update_by",fill = FieldFill.UPDATE)
    private Long updateBy;
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;
    @ApiModelProperty(value = "是否删除  0正常  1删除")
    private Integer delFlag = 0;


}
