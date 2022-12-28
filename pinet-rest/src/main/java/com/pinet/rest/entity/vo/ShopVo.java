package com.pinet.rest.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Integer orderNum = 0;

    //todo 这个值待确认
    @ApiModelProperty(value = "最大数量",notes= "比例超过30% 爆红")
    private Integer maxNum = 100;

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

    @ApiModelProperty("距离")
    private Double distance;

    @ApiModelProperty("开始营业时间")
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("startTime")
    private Date workTime;

    @ApiModelProperty("结束营业时间")
    @JsonFormat(pattern = "HH:mm")
    @JsonProperty("endTime")
    private Date finishTime;
}
