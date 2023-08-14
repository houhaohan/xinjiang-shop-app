package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@ToString
@Data
public class KryOrderTableInfo {
    @ApiModelProperty(value = "桌台ID", required = true)
    @NotNull
    private Long tableId;

    @ApiModelProperty(value = "桌台名称", required = true)
    @NotNull
    private String tableName;

    @ApiModelProperty(value = "备注")
    private String remark;

}
