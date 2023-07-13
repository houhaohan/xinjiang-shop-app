package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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
 * 商家资金流水表
 * </p>
 *
 * @author wlbz
 * @since 2023-07-12
 */
@Getter
@Setter
@TableName("b_capital_flow")
@ApiModel(value = "BCapitalFlow对象", description = "商家资金流水表")
public class BCapitalFlow{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("金额")
    private BigDecimal amount;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("下单时间")
    private Date orderTime;

    @ApiModelProperty("到账方式，1-支付宝，2-微信，3-银行卡")
    private Integer paymentWay;

    @ApiModelProperty("资金状态 1-交易成功，2-商家退款  3提现扣减")
    private Integer status;

    @ApiModelProperty("当前余额")
    private BigDecimal balance;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("主键")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建人ID")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新人ID")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "是否删除  0正常  1删除")
    private Integer delFlag = 0;


}
