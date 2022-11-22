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
 * 
 * </p>
 *
 * @author wlbz
 * @since 2022-11-21
 */
@Getter
@Setter
@ApiModel(value = "Shop对象", description = "")
public class Shop  {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("id")
    @TableId(type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty("店铺名")
    private String name;

    @ApiModelProperty("主营业务")
    private String business;

    @ApiModelProperty("联系人手机号")
    private String phone;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("是否开店，此参数后台可以设置，店铺也可自行设置，1为营业中，0为关店")
    private Integer isOpen;

    @ApiModelProperty("1为正常，0为系统关闭，此参数仅后台可传")
    private Integer status;

    @ApiModelProperty("店铺地址")
    private String shopAddress;

    @ApiModelProperty("收货地址")
    private String receiveAddress;

    @ApiModelProperty("1为免邮，2为满足金额免邮，3为不免邮")
    private Integer isFreeShipping;

    @ApiModelProperty("所需的商品金额，满足后可免邮")
    private BigDecimal freeGoodsAmount;

    @ApiModelProperty("经度")
    private BigDecimal lng;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("创建的后台人id")
    private Long adminId;

    @ApiModelProperty("创建人名")
    private String adminName;

    @ApiModelProperty("是否删除")
    private Boolean isDeleted;

    @ApiModelProperty("店铺联系人")
    private String contact;

    @ApiModelProperty("店铺电话")
    private String shopPhone;

    @ApiModelProperty("详情背景图")
    private String cover;

    @ApiModelProperty("geoHash值")
    private String geohash;

    @ApiModelProperty("客服ID")
    private String serviceId;

    @ApiModelProperty("店铺类型 1生鲜店-自营 2.生鲜店-加盟 3便利店-自营  4便利店-加盟 5综合店-自营 6综合店-加盟 7商家入驻")
    private Integer shopType;

    @ApiModelProperty("店铺推荐时间")
    private Long homeTime;

    @ApiModelProperty("配送时效类型")
    private Integer agingType;

    @ApiModelProperty("配送时效")
    private Integer shippingTime;

    @ApiModelProperty("营业开始时间")
    private String openTime;

    @ApiModelProperty("营业截止时间")
    private String restTime;

    @ApiModelProperty("是否跨天营业 1：是 0：否")
    private Integer isCross;

    @ApiModelProperty("自提间断时间")
    private Integer pickTime;

    @ApiModelProperty("是否参与优惠券活动  0.否  1.是")
    private Integer isPreferential;

    @ApiModelProperty("店铺排序")
    private Integer sort;

    @ApiModelProperty("是否自提 0为不可自提 1为可以自提")
    private Integer isSelf;

    @ApiModelProperty("自提开始时间")
    private String selfStartTime;

    @ApiModelProperty("自提结束时间")
    private String selfEndTime;

    @ApiModelProperty("疫情期间是否可以购买 0.不可购买 1.可购买")
    private Integer isBuy;


}
