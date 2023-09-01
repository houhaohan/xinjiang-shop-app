package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TableInfo {

    @ApiModelProperty(value = "桌台id，通过查询桌台信息接口获取",required = true)
    private Long tableId;

    @ApiModelProperty(value = "桌台名称",required = true)
    private String tableName;

    @ApiModelProperty("备注")
    private String remark;
}
