package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-17
 */
@Getter
@Setter
@TableName("orders")
@ApiModel(value = "Orders对象", description = "订单表")
public class Orders extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    private Long orderNo;

    @ApiModelProperty("客如云订单编号")
    private String kryOrderNo;

    @ApiModelProperty("取餐号")
    private String mealCode;

    @ApiModelProperty("订单状态  1外卖  2自提")
    private Integer orderType;

    @ApiModelProperty("订单状态 10待付款   20已支付（已下单）  30商家制作中   40商品配送中   50商品已送达   90订单已退款     99订单取消   100订单完成")
    private Integer orderStatus;

    @ApiModelProperty("下单用户id")
    private Long customerId;

    @ApiModelProperty("商家店铺id")
    private Long shopId;

    @ApiModelProperty("客如云店铺id")
    private Long kryShopId;

    @ApiModelProperty("商家店铺名称")
    private String shopName;

    @ApiModelProperty("订单总金额（商品金额+配送费）")
    private BigDecimal orderPrice;

    @ApiModelProperty("订单商品金额")
    private BigDecimal orderProdPrice;

    @ApiModelProperty("优惠金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("配送费(初版固定为3元)")
    private BigDecimal shippingFee;

    @ApiModelProperty("打包费")
    private BigDecimal packageFee;

    @ApiModelProperty("预计送达开始时间（实际下单时间推迟1-1.5个小时）")
    private Date estimateArrivalStartTime;

    @ApiModelProperty("预计送达结束时间")
    private Date estimateArrivalEndTime;

    @ApiModelProperty("实际送达时间")
    private Date actualArrivalTime;

    @ApiModelProperty("订单距离（单位:米）")
    private Integer orderDistance;

    @ApiModelProperty("订单备注")
    private String remark;

    @ApiModelProperty("分享人id")
    private Long shareId;

    @ApiModelProperty("佣金")
    private BigDecimal commission;

    @ApiModelProperty("使用的优惠券id  0表示未使用")
    private Long customerCouponId;

}
