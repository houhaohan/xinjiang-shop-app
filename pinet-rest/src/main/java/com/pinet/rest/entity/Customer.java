package com.pinet.rest.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 用户表
* @author Administrator
* @TableName customer
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Customer", description = "用户信息")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 用户id
    */
    @ApiModelProperty("用户id")
    @TableId(value = "customer_id",type = IdType.AUTO)
    private Long customerId;
    /**
    * 微信小程序唯一标识
    */
    @ApiModelProperty("微信小程序唯一标识")
    private String openId;
    /**
    * 邀请码
    */
    @ApiModelProperty("邀请码")
    private String uuid;
    /**
    * 邀请我的人
    */
    @ApiModelProperty("邀请我的人")
    private Long pid;
    /**
    * 密码(MD5(密码))
    */
    @ApiModelProperty("密码(MD5(密码))")
    private String password;
    /**
    * 昵称
    */
    @ApiModelProperty("昵称")
    private String nickname;
    /**
    *
    */
    @ApiModelProperty("")
    private String realname;
    /**
    * 手机号
    */
    @ApiModelProperty("手机号")
    private String phone;
    /**
    * 性别(0:未知,1:男,2:女)
    */
    @ApiModelProperty("性别(0:未知,1:男,2:女)")
    private Integer sex;
    /**
    * 头像
    */
    @ApiModelProperty("头像")
    private String avatar;
    /**
    *
    */
    @ApiModelProperty("")
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
    @ApiModelProperty("区域")
    private String area;
    /**
    * 注册时间
    */
    @ApiModelProperty("注册时间")
    private Long createTime;
    /**
    * 注册IP地址
    */
    @ApiModelProperty("注册IP地址")
    private String createIp;
    /**
    * 最后登录时间
    */
    @ApiModelProperty("最后登录时间")
    private Long lastLoginTime;
    /**
    * 最后登录IP地址
    */
    @ApiModelProperty("最后登录IP地址")
    private String lastLoginIp;
    /**
    * 极光推送token
    */
    @ApiModelProperty("极光推送token")
    private String jpushToken;
    /**
    * 云信的token
    */
    @ApiModelProperty("云信的token")
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
    @TableLogic(value = "0",delval = "1")
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
    @ApiModelProperty("支付密码")
    private String payPassword;
    /**
    * 环信uuid
    */
    @ApiModelProperty("环信uuid")
    private String hxUuid;
    /**
    * 是否设置支付密码，1为已设置，0为未设置
    */
    @ApiModelProperty("是否设置支付密码，1为已设置，0为未设置")
    private Integer isPayPassword;
    /**
    * 是否为新用户 0：新用户 1：老用户
    */
    @ApiModelProperty("是否为新用户 0：新用户 1：老用户")
    private Integer isNew;
    /**
    * 元宵节猜灯谜 当天是否领取了奖项 0为未领取 1为领取了
    */
    @ApiModelProperty("元宵节猜灯谜 当天是否领取了奖项 0为未领取 1为领取了")
    private Integer ridIsReceive;
    /**
    * 当天答题次数
    */
    @ApiModelProperty("当天答题次数")
    private Integer ridAnswerNum;
    /**
    *  是否为全民合伙人 0.否 1.是
    */
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

    @ApiModelProperty("轻食openID")
    private String qsOpenId;

}
