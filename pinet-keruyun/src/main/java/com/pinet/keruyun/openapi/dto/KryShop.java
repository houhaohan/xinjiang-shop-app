package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class KryShop implements Serializable {

    private static final long serialVersionUID = -3564479427608324508L;

    @ApiModelProperty(value = "商户id", required = true)
    @NotNull
    private Long shopIdenty;
    @ApiModelProperty(value = "合作方商户id", required = true)
    @NotNull
    private String tpShopId;//合作方商户id
    @ApiModelProperty(value = "商户名称", required = true)
    @NotNull
    private String shopName;//商户名称
}
