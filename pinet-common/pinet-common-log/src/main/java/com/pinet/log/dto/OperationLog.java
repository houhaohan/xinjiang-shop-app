package com.pinet.log.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Setter;

@Data
public class OperationLog {
    //服务段ip
    @ApiModelProperty("服务器ip")
    private String serverIp;
    //服务端Name
    @ApiModelProperty("服务名称")
    private String serverName;
    //服务端环境
    @ApiModelProperty("环境")
    private String serverEnv;
    //请求方法名
    @ApiModelProperty("方法名")
    private String method;
    //请求服务名
    @ApiModelProperty("服务名")
    private String service;
    //请求uri
    @ApiModelProperty("请求url")
    private String uri;
    // 请求参数
    @ApiModelProperty("请求参数")
    private String reqParam;
    // 响应参数
    private String responseContent;
    // 当前操作用户ip
    @ApiModelProperty("当前操作用户ip")
    private String currentUserIp;
    // 当前操作用户id
    @ApiModelProperty("当前操作用户id")
    private String currentUserId;
    // 当前操作用户名
    @ApiModelProperty("当前操作用户名")
    private String currentUserName;
    // 日志描述
    @ApiModelProperty("日志描述")
    private String desc;
    // 日志操作类型
    @ApiModelProperty("日志操作类型")
    private String eventType;
    // 成功为true，失败为false
    @ApiModelProperty("状态")
    private boolean succ;
    @ApiModelProperty("错误信息")
    private String errorMsg;

}
