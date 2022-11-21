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
    @ApiModelProperty("主键")
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private Long id;


    @ApiModelProperty(value = "创建人",hidden=true)
    private String createUser;
    @ApiModelProperty(value = "创建时间",hidden=true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty(value = "更新人",hidden=true)
    private String updateUser;
    @ApiModelProperty(value = "更新时间",hidden=true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
