package com.pinet.rest.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinet.rest.entity.OrderDiscount;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.Shop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 订单详情vo
 * @author: hhh
 * @create: 2022-12-12 15:00
 **/
@Data
@ApiModel(value = "OrderDetailVo",description = "订单详情vo")
public class OrderDetailVo {
    @ApiModelProperty(value = "订单id",name = "orderId")
    private Long orderId;

    @ApiModelProperty(value = "订单号",name = "orderNo")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long orderNo;

    @ApiModelProperty(value = "客如云订单号",name = "kryOrderNo")
    private String kryOrderNo;

    @ApiModelProperty(value = "取餐号",name = "mealCode")
    private String mealCode = "";

    @ApiModelProperty(value = "订单状态  1外卖  2自提",name = "orderType")
    private Integer orderType;

    @ApiModelProperty(value = "用户id",name = "customerId")
    private Long customerId;

    @ApiModelProperty(value = "订单状态",name = "orderStatus")
    private Integer orderStatus;

    @ApiModelProperty(value = "预计送达开始时间",name = "estimateArrivalStartTime")
    @JsonFormat(pattern = "HH:mm",timezone = "GMT+8")
    private Date estimateArrivalStartTime;

    @ApiModelProperty(value = "预计送达结束时间",name = "estimateArrivalEndTime")
    @JsonFormat(pattern = "HH:mm",timezone = "GMT+8")
    private Date estimateArrivalEndTime;

    @ApiModelProperty(value = "预计送达时间",name = "estimateArrivalTime")
    private String estimateArrivalTime;

    @ApiModelProperty(value = "店铺名称",name = "shopName")
    private String shopName;

    @ApiModelProperty(value = "店铺id",name = "shopId")
    private Long shopId;

    @ApiModelProperty(value = "客如云店铺id",name = "kryShopId")
    private Long kryShopId;

    @ApiModelProperty(value = "商品总价",name = "orderPrice")
    private BigDecimal orderProdPrice;

    @ApiModelProperty(value = "配送费",name = "shippingFee")
    private BigDecimal shippingFee;

    @ApiModelProperty(value = "订单总价",name = "orderPrice")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "下单时间",name = "createTime")
    @JsonFormat(pattern = "MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "订单过期时间",name = "createTime")
    private Long expireTime;

    @ApiModelProperty(value = "配送地址",name = "address")
    private String address;

    @ApiModelProperty(value = "收货人",name = "name")
    private String name;

    @ApiModelProperty(value = "收货电话",name = "tel")
    private String tel;

    @ApiModelProperty(value = "性别(1先生 2女士)",name = "sex")
    private String sex;

    @ApiModelProperty(value = "备注",name = "remark")
    private String remark;

    @ApiModelProperty(value = "商品信息",name = "orderProducts")
    private List<OrderProduct> orderProducts;

    @ApiModelProperty(value = "商品总数量",name = "prodTotalNum")
    private Integer prodTotalNum;

    @ApiModelProperty(value = "店铺信息",name = "shop")
    private Shop shop;

    @ApiModelProperty(value = "优惠明细",name = "orderDiscounts")
    private List<OrderDiscount> orderDiscounts;

    @ApiModelProperty(value = "优惠总金额",name = "discountAmount")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "打包费",name = "packageFee")
    private BigDecimal packageFee;

    @ApiModelProperty(value = "积分",name = "score")
    private Integer score;

}
