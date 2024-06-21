package com.pinet.keruyun.openapi.vo.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 会员储值响应参数
 * @author: chengshuanghui
 * @date: 2024-05-09 16:16
 */
@Data
@ApiModel("会员储值")
public class DirectChargeVO {

    @ApiModelProperty("储值账户id")
    private String accountId;
    @ApiModelProperty("储值账户余额 = 储值账户可用余额 + 储值账户预扣金额")
    private Balance remainTotalValue;
    @ApiModelProperty("储值账户可用余额")
    private Balance remainAvailableValue;
    @ApiModelProperty("储值账户预扣金额")
    private Balance preDeductValue;
    @ApiModelProperty("储值累计总额")
    private Balance totalValue;

    @Data
    @ApiModel("余额")
    public static class Balance{
        @ApiModelProperty("实储余额，单位：分")
        private Long realValue;
        @ApiModelProperty("赠储余额，单位：分")
        private Long giftValue;
        @ApiModelProperty("预储余额，单位：分")
        private Long preValue;
        @ApiModelProperty("总余额=实储余额 + 赠储余额 + 预储余额，单位：分")
        private Long totalValue;
    }
}
