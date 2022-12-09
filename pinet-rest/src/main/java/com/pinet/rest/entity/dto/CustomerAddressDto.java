package com.pinet.rest.entity.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel(value = "CustomerAddressDto",description = "收获地址")
public class CustomerAddressDto {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("姓名")
    @NotBlank(message = "收货人姓名不能为空")
    private String name;

    @ApiModelProperty("手机号")
    @NotBlank(message = "收货人手机号不能为空")
    private String phone;

    @ApiModelProperty("是否默认，0-否，1-是")
    private Integer status;

    @ApiModelProperty("省份")
    @NotBlank(message = "省份不能为空")
    private String province;

    @ApiModelProperty("城市")
    @NotBlank(message = "城市不能为空")
    private String city;

    @ApiModelProperty("区")
    @NotBlank(message = "区不能为空")
    private String district;

    @ApiModelProperty("经度")
    @NotNull(message = "经度不能为空")
    private BigDecimal lng;

    @ApiModelProperty("纬度")
    @NotNull(message = "纬度不能为空")
    private BigDecimal lat;

    @ApiModelProperty("门牌号")
    @NotBlank(message = "门牌号不能为空")
    private String houseNumber;

    @ApiModelProperty("地点名")
    @NotBlank(message = "地点名不能为空")
    private String addressName;

}
