package com.pinet.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
//    @ApiModelProperty(value = "创建人")
//    private String createUser;
    @ApiModelProperty(value = "创建人ID")
    private Long createBy;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
//    @ApiModelProperty(value = "更新人")
//    private String updateUser;
    @ApiModelProperty(value = "更新人ID")
    private Long updateBy;
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty(value = "是否删除  0正常  1删除")
    private Integer delFlag;


}
