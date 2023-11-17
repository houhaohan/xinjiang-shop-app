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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 订单配送信息表
 * </p>
 *
 * @author wlbz
 * @since 2023-09-08
 */
@Getter
@Setter
@TableName("order_logistics")
@ApiModel(value = "OrderLogistics对象", description = "订单配送信息表")
public class OrderLogistics extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("物流id")
    private String clientId;

    @ApiModelProperty("配送平台,dada-达达配送")
    private String platform;

    @ApiModelProperty("订单状态，待接单＝1,待取货＝2,配送中＝3,已完成＝4,已取消＝5, 已追加待接单=8,妥投异常之物品返回中=9")
    private Integer orderStatus;

    @ApiModelProperty("重复回传状态原因(1-重新分配骑士，2-骑士转单)")
    private Integer repeatReasonType;

    @ApiModelProperty("订单取消原因")
    private String cancelReason;

    @ApiModelProperty("订单取消原因来源(1:达达配送员取消；2:商家主动取消；3:系统或客服取消；0:默认值)")
    private Integer cancelFrom;

    @ApiModelProperty("对client_id, order_id, update_time的值进行字符串升序排列，再连接字符串，取md5值")
    private String signature;

    @ApiModelProperty("配送员id")
    private Long dmId;

    @ApiModelProperty("配送员姓名")
    private String dmName;

    @ApiModelProperty("配送员手机号")
    private String dmMobile;

    @ApiModelProperty("收货码")
    private String finishCode;

    @ApiModelProperty("配送距离(单位：米)")
    private Double distance;

    @ApiModelProperty("实际运费(单位：元)，运费减去优惠券费用")
    private Double fee;

    @ApiModelProperty("运费(单位：元)")
    private Double deliverFee;

    @ApiModelProperty("接单时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date acceptTime;

    @ApiModelProperty("取货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fetchTime;

    @ApiModelProperty("送达时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    @ApiModelProperty("取消时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;

    @ApiModelProperty("违约金")
    private Double deductFee;


}
