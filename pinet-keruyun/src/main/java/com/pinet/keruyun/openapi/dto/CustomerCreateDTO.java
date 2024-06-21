package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-05-09 14:11
 */
@Data
public class CustomerCreateDTO {

    @ApiModelProperty("性别,0女1男2其它")
    private Integer gender;

    @ApiModelProperty("手机号,11位")
    private String mobile;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("入会门店id")
    private String shopId;

    @ApiModelProperty("生日日期,yyyy-MM-dd格式")
    private String birthday;

    @ApiModelProperty("添加标签")
    private String addTag;

    @ApiModelProperty("移除标签")
    private String removeTag;

}
