package com.pinet.keruyun.openapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BrandStoreVO {

    private List<Shop> shops;

    @Data
    public static class Shop{
        @ApiModelProperty("省")
        private String addressProvince;

        @ApiModelProperty("纬度")
        private String latitude;

        @ApiModelProperty("经度")
        private String longitude;

        @ApiModelProperty("机构模式 SINGLE 代表单店 CHAIN代表连锁")
        private String orgMode;

        @ApiModelProperty("附加业态")
        private String viceMeals;

        @ApiModelProperty("机构类型 STORE代表店，BRAND代表品牌")
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

        @ApiModelProperty("市")
        private String addressCity;

        @ApiModelProperty("渠道")
        private String channelSource;

        @ApiModelProperty("主营业态")
        private String mainMeal;

        @ApiModelProperty("联系电话")
        private String contactMobile;

        @ApiModelProperty("店铺图片地址")
        private String detailPictures;

        @ApiModelProperty("门店名称")
        private String name;

        @ApiModelProperty("服务电话")
        private String serviceMobile;

        @ApiModelProperty("省")
        private String provinceName;

        @ApiModelProperty("区")
        private String addressArea;

        @ApiModelProperty("状态 NORMAL代表正常营业 DISABLE代表禁用")
        private String status;
    }
}
