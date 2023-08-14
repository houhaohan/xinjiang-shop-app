package com.pinet.keruyun.openapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhaobo
 */
@Data
@ToString
public class KryResponse<T> {
    @ApiModelProperty("结果")
    private T result;
    @ApiModelProperty("返回信息")
    private int code;
    @ApiModelProperty("返回信息")
    private String message;
    @ApiModelProperty("错误信息序列码")
    private String messageUuid;
    private String apiMessage;
}
