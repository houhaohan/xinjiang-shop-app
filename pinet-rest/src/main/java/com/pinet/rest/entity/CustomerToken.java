package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 用户登录token
 * </p>
 *
 * @author wlbz
 * @since 2022-12-12
 */
@Getter
@Setter
@TableName("customer_token")
@ApiModel(value = "CustomerToken对象", description = "用户登录token")
public class CustomerToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(type= IdType.AUTO)
    private Long id;

    @ApiModelProperty("用户id")
    private Long customerId;

    private String token;

    @ApiModelProperty("终端")
    private Integer terminal;

    @ApiModelProperty("是否已加入黑名单，注意，就算加入了黑名单，但3分钟内仍可以使用此token进行请求")
    private Integer isBlackmail;

    private Long expireTime;

    private Long createTime;

    @ApiModelProperty("token过期后仍可访问的时间，防止出线一过期就不可使用此token")
    private Long graceTime;


}
