package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public class Delivery implements Serializable {

    private static final long serialVersionUID = -6844344203756741118L;

    @ApiModelProperty(value = "期望送达时间，外送必填，时间戳，精确到秒，为0则立即送餐", required = true)
    @NotNull
    @Min(value = 0L)
    private Long expectTime;    //期望送达时间，外送必填，时间戳，精确到秒，为0则立即送餐

    @ApiModelProperty(value = "配送方式")
    @NotNull
    private Integer deliveryParty;    //配送方：1-商家自配送，2-外卖来源平台配送

    @ApiModelProperty(value = "顾客姓名", required = true)
    @NotNull
    private String receiverName;    //顾客姓名

    @ApiModelProperty(value = "顾客电话", required = true)
    @NotNull
    private String receiverPhone;    //顾客电话

    @ApiModelProperty(value = "顾客性别，1男 ,0女,-1未知")
    private Integer receiverGender;        //顾客性别 1男 ,0女,-1未知

    @ApiModelProperty("配送员")
    private String delivererName;    //配送员，非商家自配送时必填

    @ApiModelProperty("配送员电话")
    private String delivererPhone;    //配送员电话

    @ApiModelProperty(value = "送货地址")
    private String delivererAddress;    //送货地址

    @ApiModelProperty(value = "经纬度类型")
    private Integer coordinateType;

    @ApiModelProperty(value = "经度")
    @DecimalMax(value = "180")
    @DecimalMin(value = "-180")
    private Double longitude;    //送餐地址经度

    @ApiModelProperty(value = "纬度")
    @DecimalMax(value = "90")
    @DecimalMin(value = "-90")
    private Double latitude;    //送餐地址纬度
}
