package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString
@NoArgsConstructor
public class ProductProperty {
    @ApiModelProperty(value = "菜品属性ID")
    private Long id;

    @ApiModelProperty(value = "菜品属性名称", required = true)
    @NotNull
    @NotBlank
    private String name;
    /**
     * 属性类型 : 属性类型 1：口味，做法 2：标签 3：备注 4：菜品属性
     */
    @ApiModelProperty(value = "属性类型1：做法 2：标签 3：备注 4：规格", required = true)
    @NotNull
    private Integer type;

    @ApiModelProperty(value = "变价, 单位：分，正数加价，负数减价，type为做法此字段必填")
    private Integer reprice;
}
