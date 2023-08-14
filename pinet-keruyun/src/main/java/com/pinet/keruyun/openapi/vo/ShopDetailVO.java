package com.pinet.keruyun.openapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShopDetailVO {
    @ApiModelProperty("省")
    private String addressProvince;
    @ApiModelProperty("纬度")
    private String latitude;
    @ApiModelProperty("机构类型：单店SINGLE,连锁CHAIN")
    private String orgMode;
    @ApiModelProperty("附加业态")
    private String viceMeals;
    @ApiModelProperty("机构类别")
    private String orgType;
    @ApiModelProperty("地址详情")
    private String addressDetail;
    @ApiModelProperty("客如云门店id")
    private Long shopId;
    @ApiModelProperty("市")
    private String cityName;
    @ApiModelProperty("区")
    private String areaName;
    @ApiModelProperty("联系人")
    private String contact;
    @ApiModelProperty("经度")
    private String longitude;
    @ApiModelProperty("城市")
    private String addressCity;
    @ApiModelProperty("主营业态")
    private String mainMeal;
    @ApiModelProperty("联系电话")
    private String contactMobile;
    @ApiModelProperty("门店图片地址")
    private String detailPictures;
    @ApiModelProperty("门店名称")
    private String name;
    @ApiModelProperty("服务电话")
    private String serviceMobile;
    @ApiModelProperty("省")
    private String provinceName;
    @ApiModelProperty("地区")
    private String addressArea;
    @ApiModelProperty("门店状态：正常NORMAL,禁用DISABLE")
    private String status;
    @ApiModelProperty("服务编码")
    private String serverCode;
    @ApiModelProperty("服务消息ID")
    private String messageId;
}
