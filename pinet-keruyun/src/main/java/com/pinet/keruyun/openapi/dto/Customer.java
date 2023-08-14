package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@ToString
@Data
public class Customer {

    @ApiModelProperty(value = "会员ID")
    private Long id;

    @ApiModelProperty(value = "会员电话", required = true)
    @NotNull
    private String phoneNumber;

    @ApiModelProperty(value = "会员姓名", required = true)
    @NotNull
    private String name;


    @ApiModelProperty(value = "会员性别", required = true)
    private Integer gender;

}
