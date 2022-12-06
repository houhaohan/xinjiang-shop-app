package com.pinet.rest.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 用户表
* @author Administrator
* @TableName customer
*/
@Data
public class Customer implements Serializable {

    /**
    * 用户id
    */
    @NotNull(message="[用户id]不能为空")
    @ApiModelProperty("用户id")
    @TableId(value = "customerId",type = IdType.AUTO)
    private Long customerId;
    /**
    * 微信小程序唯一标识
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("微信小程序唯一标识")
    @Length(max= 255,message="编码长度不能超过255")
    private String openId;
    /**
    * 邀请码
    */
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty("邀请码")
    @Length(max= 16,message="编码长度不能超过16")
    private String uuid;
    /**
    * 邀请我的人
    */
    @ApiModelProperty("邀请我的人")
    private Long pid;
    /**
    * 密码(MD5(密码))
    */
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("密码(MD5(密码))")
    @Length(max= 100,message="编码长度不能超过100")
    private String password;
    /**
    * 昵称
    */
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("昵称")
    @Length(max= 20,message="编码长度不能超过20")
    private String nickname;
    /**
    * 
    */
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty("")
    @Length(max= 16,message="编码长度不能超过16")
    private String realname;
    /**
    * 手机号
    */
    @Size(max= 16,message="编码长度不能超过16")
    @ApiModelProperty("手机号")
    @Length(max= 16,message="编码长度不能超过16")
    private String phone;
    /**
    * 性别(0:未知,1:男,2:女)
    */
    @NotNull(message="[性别(0:未知,1:男,2:女)]不能为空")
    @ApiModelProperty("性别(0:未知,1:男,2:女)")
    private Integer sex;
    /**
    * 头像
    */
    @Size(max= 512,message="编码长度不能超过512")
    @ApiModelProperty("头像")
    @Length(max= 512,message="编码长度不能超过512")
    private String avatar;
    /**
    * 
    */
    @Size(max= 64,message="编码长度不能超过64")
    @ApiModelProperty("")
    @Length(max= 64,message="编码长度不能超过64")
    private String email;
    /**
    * 
    */
    @ApiModelProperty("")
    private Long birthday;
    /**
    * 0为未认证，1为审核中，2为已认证
    */
    @ApiModelProperty("0为未认证，1为审核中，2为已认证")
    private Integer isIdentity;
    /**
    * 1-可用，0-禁用
    */
    @NotNull(message="[1-可用，0-禁用]不能为空")
    @ApiModelProperty("1-可用，0-禁用")
    private Integer active;
    /**
    * 省id
    */
    @ApiModelProperty("省id")
    private Integer provinceId;
    /**
    * 市id
    */
    @ApiModelProperty("市id")
    private Integer cityId;
    /**
    * 区域
    */
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("区域")
    @Length(max= 32,message="编码长度不能超过32")
    private String area;
    /**
    * 注册时间
    */
    @NotNull(message="[注册时间]不能为空")
    @ApiModelProperty("注册时间")
    private Long createTime;
    /**
    * 注册IP地址
    */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("注册IP地址")
    @Length(max= 50,message="编码长度不能超过50")
    private String createIp;
    /**
    * 最后登录时间
    */
    @ApiModelProperty("最后登录时间")
    private Long lastLoginTime;
    /**
    * 最后登录IP地址
    */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("最后登录IP地址")
    @Length(max= 50,message="编码长度不能超过50")
    private String lastLoginIp;
    /**
    * 极光推送token
    */
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("极光推送token")
    @Length(max= 128,message="编码长度不能超过128")
    private String jpushToken;
    /**
    * 云信的token
    */
    @Size(max= 128,message="编码长度不能超过128")
    @ApiModelProperty("云信的token")
    @Length(max= 128,message="编码长度不能超过128")
    private String yxToken;
    /**
    * 终端
    */
    @ApiModelProperty("终端")
    private Integer terminal;
    /**
    * 是否被删除，1为删除，0为未被删除
    */
    @ApiModelProperty("是否被删除，1为删除，0为未被删除")
    private Integer isDeleted;
    /**
    * 是否需要推送，默认为1
    */
    @ApiModelProperty("是否需要推送，默认为1")
    private Integer needPush;
    /**
    * 余额
    */
    @ApiModelProperty("余额")
    private BigDecimal balance;
    /**
    * 总充值余额
    */
    @ApiModelProperty("总充值余额")
    private BigDecimal totalRecharge;
    /**
    * 支付密码
    */
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("支付密码")
    @Length(max= 32,message="编码长度不能超过32")
    private String payPassword;
    /**
    * 环信uuid
    */
    @Size(max= 40,message="编码长度不能超过40")
    @ApiModelProperty("环信uuid")
    @Length(max= 40,message="编码长度不能超过40")
    private String hxUuid;
    /**
    * 是否设置支付密码，1为已设置，0为未设置
    */
    @ApiModelProperty("是否设置支付密码，1为已设置，0为未设置")
    private Integer isPayPassword;
    /**
    * 是否为新用户 0：新用户 1：老用户
    */
    @NotNull(message="[是否为新用户 0：新用户 1：老用户]不能为空")
    @ApiModelProperty("是否为新用户 0：新用户 1：老用户")
    private Integer isNew;
    /**
    * 元宵节猜灯谜 当天是否领取了奖项 0为未领取 1为领取了
    */
    @NotNull(message="[元宵节猜灯谜 当天是否领取了奖项 0为未领取 1为领取了]不能为空")
    @ApiModelProperty("元宵节猜灯谜 当天是否领取了奖项 0为未领取 1为领取了")
    private Integer ridIsReceive;
    /**
    * 当天答题次数
    */
    @NotNull(message="[当天答题次数]不能为空")
    @ApiModelProperty("当天答题次数")
    private Integer ridAnswerNum;
    /**
    *  是否为全民合伙人 0.否 1.是
    */
    @NotNull(message="[ 是否为全民合伙人 0.否 1.是]不能为空")
    @ApiModelProperty(" 是否为全民合伙人 0.否 1.是")
    private Integer isPartner;
    /**
    * 总积分
    */
    @ApiModelProperty("总积分")
    private BigDecimal totalIntegral;
    /**
    * 已使用积分
    */
    @ApiModelProperty("已使用积分")
    private BigDecimal usedIntegral;
    /**
    * 个性推荐，0关闭，1开启
    */
    @ApiModelProperty("个性推荐，0关闭，1开启")
    private Integer characterSet;
    /**
    * 最近修改密码或者绑定账户的时间
    */
    @ApiModelProperty("最近修改密码或者绑定账户的时间")
    private Long updateTime;

}
