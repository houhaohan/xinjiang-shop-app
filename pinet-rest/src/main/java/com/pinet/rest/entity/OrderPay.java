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
 * 支付记录表
 * </p>
 *
 * @author chengshuanghui
 * @since 2022-12-20
 */
@Getter
@Setter
@TableName("order_pay")
@ApiModel(value = "OrderPay对象", description = "支付记录表")
public class OrderPay extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("支付类型 1-轻食订单支付  2-店帮主充值 3-VIP支付")
    private Integer payType;

    @ApiModelProperty("订单编号")
    private Long orderNo;

    @ApiModelProperty("支付的用户id")
    private Long customerId;

    @ApiModelProperty("支付状态(1未支付  2已支付)")
    private Integer payStatus;

    @ApiModelProperty("订单总金额")
    private BigDecimal orderPrice;

    @ApiModelProperty("支付总金额")
    private BigDecimal payPrice;

    @ApiModelProperty("支付时间")
    private Date payTime;

    @ApiModelProperty("预支付id")
    private String prepayId;

    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty("渠道id")
    private String channelId;

    @ApiModelProperty("渠道名称")
    private String payName;

    @ApiModelProperty("三方订单号")
    private String outTradeNo;

    @ApiModelProperty("用户支付ip")
    private String ip;


}
