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
 * 地址管理表
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Getter
@Setter
@TableName("customer_address")
@ApiModel(value = "CustomerAddress对象", description = "地址管理表")
public class CustomerAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long customerId;

    @ApiModelProperty("省")
    private Integer provinceId;

    @ApiModelProperty("市")
    private Integer cityId;

    @ApiModelProperty("区")
    private Integer districtId;

    @ApiModelProperty("姓名")
    private String name;

    private String address;

    private String province;

    private String city;

    private String district;

    @ApiModelProperty("邮编")
    private String postcode;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("电话")
    private String tel;

    private Integer active;

    @ApiModelProperty("默认地址值为1")
    private Integer status;

    @ApiModelProperty("经度")
    private BigDecimal lng;

    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("门牌号")
    private String houseNumber;

    @ApiModelProperty("地点名")
    private String addressName;


}
