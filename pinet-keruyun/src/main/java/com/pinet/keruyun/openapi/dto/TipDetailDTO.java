package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TipDetailDTO {
    /**
     * 类型，1：固定金额，2：按比例
     */
    @ApiModelProperty(value = "类型，1：固定金额，2：按比例", required = true)
    @NotNull
    private Integer type;
    /**
     * 小费金额，单位：分
     */
    @ApiModelProperty(value = "小费金额，单位：分", required = true)
    @NotNull
    private Integer amount;
    /**
     * 小费费率，取值1-100，type为2按比例时此字段必传
     */
    @ApiModelProperty(value = "小费费率，取值1-100，type为2按比例时此字段必传")
    private Integer rate;

}
