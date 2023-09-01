package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel
@Data
public class OrderDetailDTO {

    @ApiModelProperty(value = "门店", required = true)
    @NotNull
    private Long shopIdenty;
    @ApiModelProperty(value = "订单号", required = true)
    @NotNull
    @Size(min = 1, max = 20)
    private List<Long> ids;

}
