package com.pinet.rest.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pinet.rest.entity.Coupon;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CustomerCouponListVo {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long customerId;

    @ApiModelProperty("优惠券ID")
    private Long couponId;

    @ApiModelProperty("1-全部店铺，2-部分店铺")
    private Integer useShop;

    @ApiModelProperty("过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expireTime;

    @ApiModelProperty("优惠券名称")
    private String couponName;

    @ApiModelProperty("优惠券类型  1满减券 2-折扣券")
    private Integer couponType;

    @ApiModelProperty("店铺id  0表示所有店可用")
    private Long shopId;

    @ApiModelProperty("是否可用")
    private Boolean isUsable;

    @ApiModelProperty("门槛金额")
    private BigDecimal thresholdAmount;

    @ApiModelProperty("优惠券金额")
    private BigDecimal couponAmount;

    @ApiModelProperty("折扣比例%")
    private Integer discount;

    @ApiModelProperty("1未领取  2已领取  3已放弃 4已使用")
    private Integer couponStatus;

    @ApiModelProperty("使用规则")
    private String rule;

    private String getRule() {
        String msg2 = "2、本券一次使用一张,不限制商品,不可抵扣配送费及零星选配的辅料等附加费用";
        String msg3 = "3、本券不于其他优惠同享。(店帮主可与本券同使用)";

        StringBuilder msg = new StringBuilder();
        if (this.useShop == 2) {
            msg.append("1、本券可用于部分门店使用,享受门店所有优惠。").append("\r\n").append(msg2)
                    .append("\r\n").append(msg3);
        } else {
            msg.append("1、本券全国门店通用,(部分特殊活动门店除外),下单前可与客服确认门店是否支持使用。").append("\r\n").append(msg2)
                    .append("\r\n").append(msg3);
        }
        return msg.toString();
    }
}
