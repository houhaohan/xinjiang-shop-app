package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 订单退款记录表
 * </p>
 *
 * @author wlbz
 * @since 2023-01-04
 */
@Getter
@Setter
@TableName("order_refund")
@ApiModel(value = "OrderRefund对象", description = "订单退款记录表")
public class OrderRefund extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("退款编号")
    private Long refundNo;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("支付记录id")
    private Long orderPayId;

    @ApiModelProperty("退款金额")
    private BigDecimal refundPrice;

    @ApiModelProperty("订单总金额")
    private BigDecimal orderPrice;

    @ApiModelProperty("是否全额退款")
    private Boolean isAllRefund;

    @ApiModelProperty("退款描述(退款原因)")
    private String refundDesc;

    @ApiModelProperty("退款状态  1未到账  2已到账")
    private Integer refundStatus;


}
