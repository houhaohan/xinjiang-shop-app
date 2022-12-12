package com.pinet.rest.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinet.rest.entity.bo.OrderProductBo;
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
    private Long orderNo;

    @ApiModelProperty(value = "用户id",name = "customerId")
    private Long customerId;

    @ApiModelProperty(value = "订单状态",name = "orderStatus")
    private Integer orderStatus;

    @ApiModelProperty(value = "预计送达开始时间",name = "estimateArrivalStartTime")
    @JsonFormat(pattern = "HH:mm")
    private Date estimateArrivalStartTime;

    @ApiModelProperty(value = "预计送达结束时间",name = "estimateArrivalEndTime")
    @JsonFormat(pattern = "HH:mm")
    private Date estimateArrivalEndTime;

    @ApiModelProperty(value = "店铺名称",name = "shopName")
    private String shopName;

    @ApiModelProperty(value = "店铺id",name = "shopId")
    private Long shopId;

    @ApiModelProperty(value = "商品总价",name = "orderPrice")
    private BigDecimal orderProdPrice;

    @ApiModelProperty(value = "配送费",name = "shippingFee")
    private BigDecimal shippingFee;

    @ApiModelProperty(value = "订单总价",name = "orderPrice")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "下单时间",name = "createTime")
    @JsonFormat(pattern = "MM-dd HH:mm:ss")
    private Date createTime;

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

    @ApiModelProperty(value = "商品信息",name = "orderProductBoList")
    private List<OrderProductBo> orderProductBoList;

}
