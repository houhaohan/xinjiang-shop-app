package com.pinet.rest.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pinet.core.constants.CommonConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author: bj
 * @create: 2022/12/12 14:28
 */
@Data
@ApiModel(value = "ShopVo",description = "用户信息")
public class ShopVo {

    @ApiModelProperty("店铺id")
    private Long id;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("订单数量")
    private int orderNum;

    @ApiModelProperty(value = "最大数量",notes= "比例超过50% 爆红")
    private Integer maxNum = CommonConstant.MAX_ORDER_NUM;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String district;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("经度")
    private String lng;

    @ApiModelProperty("纬度")
    private String lat;

    @ApiModelProperty(value = "距离",example = "10.23")
    private Double distance;

    @ApiModelProperty("开始营业时间")
    @JsonFormat(pattern = "HH:mm",timezone = "GMT+8")
    @JsonProperty("startTime")
    private Date workTime;

    @ApiModelProperty("结束营业时间")
    @JsonFormat(pattern = "HH:mm",timezone = "GMT+8")
    @JsonProperty("endTime")
    private Date finishTime;
}
