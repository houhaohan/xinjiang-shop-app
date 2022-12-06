package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Getter
@Setter
@ApiModel(value = "Customer对象", description = "用户表")
public class Customer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @TableId(value = "customer_id", type = IdType.AUTO)
    private Long customerId;

    @ApiModelProperty("微信小程序唯一标识")
    private String openId;

    @ApiModelProperty("邀请码")
    private String uuid;

    @ApiModelProperty("邀请我的人")
    private Long pid;

    @ApiModelProperty("密码(MD5(密码))")
    private String password;

    @ApiModelProperty("昵称")
    private String nickname;

    private String realname;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("性别(0:未知,1:男,2:女)")
    private Integer sex;

    @ApiModelProperty("头像")
    private String avatar;

    private String email;

    private Long birthday;

    @ApiModelProperty("0为未认证，1为审核中，2为已认证")
    private Integer isIdentity;

    @ApiModelProperty("1-可用，0-禁用")
    private Integer active;

    @ApiModelProperty("省id")
    private Integer provinceId;

    @ApiModelProperty("市id")
    private Integer cityId;

    @ApiModelProperty("区域")
    private String area;

    @ApiModelProperty("注册IP地址")
    private String createIp;

    @ApiModelProperty("最后登录时间")
    private Long lastLoginTime;

    @ApiModelProperty("最后登录IP地址")
    private String lastLoginIp;

    @ApiModelProperty("极光推送token")
    private String jpushToken;

    @ApiModelProperty("云信的token")
    private String yxToken;

    @ApiModelProperty("终端")
    private Integer terminal;

    @ApiModelProperty("是否被删除，1为删除，0为未被删除")
    private Boolean isDeleted;

    @ApiModelProperty("是否需要推送，默认为1")
    private Boolean needPush;

    @ApiModelProperty("余额")
    private BigDecimal balance;

    @ApiModelProperty("总充值余额")
    private BigDecimal totalRecharge;

    @ApiModelProperty("支付密码")
    private String payPassword;

    @ApiModelProperty("环信uuid")
    private String hxUuid;

    @ApiModelProperty("是否设置支付密码，1为已设置，0为未设置")
    private Boolean isPayPassword;

    @ApiModelProperty("是否为新用户 0：新用户 1：老用户")
    private Integer isNew;

    @ApiModelProperty("元宵节猜灯谜 当天是否领取了奖项 0为未领取 1为领取了")
    private Integer ridIsReceive;

    @ApiModelProperty("当天答题次数")
    private Integer ridAnswerNum;

    @ApiModelProperty(" 是否为全民合伙人 0.否 1.是")
    private Integer isPartner;

    @ApiModelProperty("总积分")
    private BigDecimal totalIntegral;

    @ApiModelProperty("已使用积分")
    private BigDecimal usedIntegral;

    @ApiModelProperty("个性推荐，0关闭，1开启")
    private Integer characterSet;


}
