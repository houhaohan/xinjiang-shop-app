package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 客如云订单补偿表
 * </p>
 *
 * @author wlbz
 * @since 2023-09-07
 */
@Getter
@Setter
@TableName("kry_order_compensate")
@ApiModel(value = "KryOrderCompensate对象", description = "客如云订单补偿表")
public class KryOrderCompensate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("客如云订单编号")
    private String kryOrderNo;

    @ApiModelProperty("订单重试状态  0-推送失败，1-推送成功")
    private Integer status;

    @ApiModelProperty("推送次数")
    private Integer times;

    @ApiModelProperty("推送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date pushTime;

    @ApiModelProperty("推送失败原因")
    private String failReason;


}
