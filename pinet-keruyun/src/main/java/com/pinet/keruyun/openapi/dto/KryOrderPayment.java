package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Data
@ToString
public class KryOrderPayment implements Serializable {

    private static final long serialVersionUID = 2567904614162133556L;
    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "券id")
    private String promotionId;

    @ApiModelProperty(value = "卡号")
    private String memberCardNumber;

    @ApiModelProperty(value = "密码")
    private String memberPassword;

    @ApiModelProperty(value = "平台优惠总金额，单位：分", required = true)
    @NotNull
    @Min(value = 0)
    private Integer platformDiscountFee;//平台优惠总金额

    @ApiModelProperty(value = "商家优惠总金额，单位：分", required = true)
    @NotNull
    @Min(value = 0)
    private Integer shopDiscountFee;//商家优惠总金额

    @ApiModelProperty(value = "订单总价，单位：分", required = true)
    @NotNull
    @Min(value = 0)
    private Integer totalFee;        //订单总价，单位：分

    @ApiModelProperty(value = "合作方向商家收取的抽佣金额，单位：分", required = true)
    @NotNull
    @Min(value = 0)
    private Integer serviceFee;

    @ApiModelProperty(value = "顾客实际付款金额=订单总价-活动优惠总金额，单位：分", required = true)
    @NotNull
    @Min(value = 0)
    private Integer userFee;

    @ApiModelProperty(value = "商户实际收款=订单总价-商家承担优惠金额-服务费+补贴，单位：分", required = true)
    @NotNull
    @Min(value = 0)
    private Integer shopFee;
    @ApiModelProperty(value = "配送费,用户自提配送费给0，单位：分", required = true)
    @NotNull
    @Min(value = 0)
    private Integer deliveryFee;    //配送费，单位：分

    @ApiModelProperty(value = "餐盒费，单位：分", required = true)
    @NotNull
    @Min(value = 0)
    private Integer packageFee;        //餐盒费，单位：分

    @ApiModelProperty(value = "外卖：优惠总金额，单位：分", required = true)
    @Min(value = 0)
    private Integer discountFee;    //优惠总金额，单位：分

    @ApiModelProperty(value = "快餐：优惠总金额，单位：分", required = true)
    @Min(value = 0)
    private Integer totalDiscountFee;    //优惠总金额，单位：分

    @ApiModelProperty(value = "平台补贴(第三方平台每单给与商家的补贴)，单位：分", required = true)
    @NotNull
    @Min(value = 0)
    private Integer subsidies;

    @ApiModelProperty(value = "1:线下支付/货到付款 2:在线支付  3:会员卡余额 4:优惠券", required = true)
    @NotNull
    private Integer payType;

}
