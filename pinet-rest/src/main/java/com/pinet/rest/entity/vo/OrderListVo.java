package com.pinet.rest.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.bo.OrderProductBo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 订单列表vo
 * @author: hhh
 * @create: 2022-12-12 13:46
 **/
@Data
@ApiModel(value = "OrderListVo",description = "订单列表vo")
public class OrderListVo {

    @ApiModelProperty(value = "订单id",name = "orderId")
    private Long orderId;

    @ApiModelProperty(value = "订单编号",name = "orderNo")
    private Long orderNo;

    @ApiModelProperty(value = "订单类型(1外卖  2自提)",name = "orderType")
    private Integer orderType;

    @ApiModelProperty(value = "商铺名称",name = "shopName")
    private String shopName;

    @ApiModelProperty(value = "商铺id",name = "shopId")
    private Long shopId;

    @ApiModelProperty(value = "订单状态",name = "orderStatus")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单状态Str",name = "orderStatusStr")
    private String orderStatusStr;

    @ApiModelProperty(value = "下单时间",name = "createTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "商品数量",name = "prodNum")
    private Integer prodNum;

    @ApiModelProperty(value = "订单总价",name = "orderPrice")
    private BigDecimal orderPrice;

    @ApiModelProperty(value = "订单商品信息",name = "orderProducts")
    private List<OrderProduct> orderProducts;
}
