package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DinnerOrderMember {

    @ApiModelProperty("会员customerId")
    private String id;

    @ApiModelProperty("会员电话")
    private String phoneNumber;

    @ApiModelProperty("会员姓名")
    private String name;

    @ApiModelProperty("会员性别")
    private Integer gender;
}
