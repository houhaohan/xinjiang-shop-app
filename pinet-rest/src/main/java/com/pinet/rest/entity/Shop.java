package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * id
     */
    private Long id;

    /**
     * 店铺名
     */
    private String name;

    /**
     * 主营业务
     */
    private String business;

    /**
     * 联系人手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 是否开店，此参数后台可以设置，店铺也可自行设置，1为营业中，0为关店
     */
    private int isOpen;

    /**
     * 1为正常，0为系统关闭，此参数仅后台可传
     */
    private Integer status;

    /**
     * create_time
     */
    private Long createTime;

    /**
     * update_time
     */
    private Long updateTime;

    /**
     * 店铺地址
     */
    private String shopAddress;

    /**
     * 收货地址
     */
    private String receiveAddress;

    /**
     * 1为免邮，2为满足金额免邮，3为不免邮
     */
    private Integer isFreeShipping;

    /**
     * 所需的商品金额，满足后可免邮
     */
    private BigDecimal freeGoodsAmount;

    /**
     * 经度
     */
    private BigDecimal lng;

    /**
     * 纬度
     */
    private BigDecimal lat;

    /**
     * 创建的后台人id
     */
    private Long adminId;

    /**
     * 创建人名
     */
    private String adminName;

    /**
     * 是否删除
     */
    private int isDeleted;

    /**
     * 店铺联系人
     */
    private String contact;

    /**
     * 店铺电话
     */
    private String shopPhone;

    /**
     * 详情背景图
     */
    private String cover;

    /**
     * geohash值
     */
    private String geohash;

    /**
     * 客服id
     */
    private String serviceId;

    /**
     * 店铺类型 1生鲜店-自营 2.生鲜店-加盟 3便利店-自营 4便利店-加盟 5综合店-自营 6综合店-加盟 7商家入驻
     */
    private Integer shopType;

    /**
     * 店铺推荐时间
     */
    private Long homeTime;

    /**
     * 配送时效类型
     */
    private Integer agingType;

    /**
     * 配送时效
     */
    private Integer shippingTime;

    /**
     * 营业开始时间
     */
    private String openTime;

    /**
     * 营业截止时间
     */
    private String restTime;

    /**
     * 是否跨天营业 1：是 0：否
     */
    private Integer isCross;

    /**
     * 自提间断时间
     */
    private Integer pickTime;

    /**
     * 是否参与优惠券活动 0.否 1.是
     */
    private Integer isPreferential;

    /**
     * 店铺排序
     */
    private Integer sort;

    /**
     * 是否自提 0为不可自提 1为可以自提
     */
    private int isSelf;

    /**
     * 自提开始时间
     */
    private String selfStartTime;

    /**
     * 自提结束时间
     */
    private String selfEndTime;

    /**
     * 疫情期间是否可以购买 0.不可购买 1.可购买
     */
    private int isBuy;

    public Shop() {}

}
