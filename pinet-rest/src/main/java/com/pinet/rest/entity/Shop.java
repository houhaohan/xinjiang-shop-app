package com.pinet.rest.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 店铺表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Getter
@Setter
@ApiModel(value = "Shop对象", description = "店铺表")
public class Shop extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺头像")
    private String avatar;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String district;

    @ApiModelProperty("省id")
    private Integer provinceId;

    @ApiModelProperty("市id")
    private Integer cityId;

    @ApiModelProperty("区id")
    private Integer districtId;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("经度")
    private String lng;

    @ApiModelProperty("纬度")
    private String lat;

    @ApiModelProperty("店铺联系人")
    private String linkman;

    @ApiModelProperty("店铺联系电话")
    private String shopPhone;

    @ApiModelProperty("店铺状态  1正常  2歇业")
    private Integer shopStatus;

    @ApiModelProperty("开始营业时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date workTime;

    @ApiModelProperty("结束营业时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

}
