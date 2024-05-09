package com.pinet.keruyun.openapi.vo.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description: 会员资产
 * @author: chengshuanghui
 * @date: 2024-05-09 15:45
 */
@Data
public class CustomerPropertyVO {
    @ApiModelProperty("可使用有效券张数")
    private Integer normalVoucherInstanceCount;
    @ApiModelProperty("会员基础信息")
    private CustomerDTO customerDTO;
    @ApiModelProperty("卡列表")
    private List<PosCardDTO> posCardDTOList;
    @ApiModelProperty("积分账户")
    private PointAccountDTO pointAccountDTO;

    @Data
    public static class CustomerDTO{
        @ApiModelProperty("用户ID")
        private String customerId;
        @ApiModelProperty("用户状态,1代表启用,0代表停用")
        private Integer state;
        @ApiModelProperty("手机号")
        private String mobile;
    }

    @Data
    @ApiModel("会员卡信息")
    public static class PosCardDTO{
        @ApiModelProperty("卡状态，SOLD：已出售；ACTIVED：已激活；STOP：已停用；INVALID：已作废；EXPIRED：已过期；REFUND：已退卡")
        private String status;
        @ApiModelProperty("卡号id")
        private String cardId;
        @ApiModelProperty("卡类型，MEMBER：会员卡；GIFT：礼品卡；PAY_MEMBER：付费会员卡")
        private String cardType;
        @ApiModelProperty("储值账户")
        private List posRechargeAccountList;
    }

    @Data
    @ApiModel("储值账户信息")
    public static class PosRechargeAccount{

        @ApiModelProperty("当前剩余可用储值")
        private Object remainAvailableValue;
    }

    @Data
    public static class RemainAvailable{
        @ApiModelProperty("当前剩余可用储值总金额 = 当前剩余可用实储总金额 + 当前剩余可用赠储总金额，单位：分")
        private Integer totalValue;
        @ApiModelProperty("当前剩余可用实储总金额，单位：分")
        private Integer realValue;
        @ApiModelProperty("当前剩余可用赠储总金额，单位：分")
        private Integer giftValue;
    }

    @Data
    public static class PointAccountDTO{
        @ApiModelProperty("当前剩余可用积分")
        private Integer remainAvailableValue;

    }
}
