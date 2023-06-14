package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 会员中心vo
 * @author: hhh
 * @create: 2023-06-14 11:07
 **/
@Data
public class MemberVo {
    @ApiModelProperty(value = "今日下单数")
    private Integer todayOrderCount;

    @ApiModelProperty(value = "今日收益")
    private BigDecimal todaySumPrice;

    @ApiModelProperty(value = "昨日收益")
    private BigDecimal yesterdaySumPrice;

    @ApiModelProperty(value = "累计充值金额")
    private BigDecimal sumRechargePrice;

    @ApiModelProperty(value = "会员等级 0门客 10普通会员  20店帮主")
    private Integer memberLevel;

    @ApiModelProperty(value = "会员等级Str")
    private String memberLevelStr;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String avatar;
}
