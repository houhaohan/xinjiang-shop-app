package com.pinet.keruyun.openapi.vo.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-05-09 15:26
 */
@Data
public class CustomerQueryVO {

    @ApiModelProperty("生日日期,yyyy-MM-dd格式")
    private String birthday;

    @ApiModelProperty("用户id")
    private String customerId;

    @ApiModelProperty("用户性别,0代表女,1代表男,2代表其他")
    private Integer gender;

    @ApiModelProperty("等级对象")
    private LevelDTO levelDTO;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("用户名称")
    private String name;

    @ApiModelProperty("用户状态,1代表启用,0代表停用")
    private Integer state;

    @Data
    public static class LevelDTO{

        @ApiModelProperty("等级级别")
        private String levelNo;

        @ApiModelProperty("等级名称")
        private String levelName;
    }
}
