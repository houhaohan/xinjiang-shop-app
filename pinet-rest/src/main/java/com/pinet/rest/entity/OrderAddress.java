package com.pinet.rest.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pinet.core.entity.BaseEntity;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Getter
@Setter
@TableName("order_address")
@ApiModel(value = "OrderAddress对象", description = "")
public class OrderAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    private Long orderId;

    @ApiModelProperty("省id")
    private Integer provinceId;

    @ApiModelProperty("市id")
    private Integer cityId;

    @ApiModelProperty("区id")
    private Integer districtId;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("区")
    private String district;

    @ApiModelProperty("邮编")
    private String postCode;

    private String lat;

    private String lng;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("电话")
    private String tel;

    @ApiModelProperty("1先生 2女士")
    private String sex;


}
