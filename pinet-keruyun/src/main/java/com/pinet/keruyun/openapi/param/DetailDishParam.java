package com.pinet.keruyun.openapi.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DetailDishParam {

    @ApiModelProperty("菜品ID，上限5个")
    private List<String> dishIds;

}
