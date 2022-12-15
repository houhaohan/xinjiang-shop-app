package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseDto {

    @ApiModelProperty("id")
    private Long id;
}
