package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExtraFeeRequest {

    @ApiModelProperty("外部附加费费用明细ID，无业务含义，标识唯一行")
    private String outExtraFeeDetailNo;

    @ApiModelProperty("附加费类型， SERVICE_FEE(“SERVICE_FEE”, “服务费”), " +
            "DELIVERY_FEE(“DELIVERY_FEE”, “配送费”), " +
            "PRACTICE_RAISE_FEE(“PRACTICE_RAISE_FEE”, “做法加价”), " +
            "/** @deprecated */ @Deprecated SCRAP_FEE(“SCRAP_FEE”, “抹零 (负数)”), " +
            "DEPOSIT_FEE(“DEPOSIT_FEE”, “押金”)")
    private String extraFeeType;

    @ApiModelProperty("附加费自定义附加费名称")
    private String customExtraFeeName;

    @ApiModelProperty("附加费的总费用")
    private Long extraTotalFee;

    @ApiModelProperty("附加费的应付金额")
    private Long extraActualFee;

    @ApiModelProperty("附加费的优惠部分")
    private Long extraPromoFee;

    @ApiModelProperty("费用参与计算方式，如固定计算、比例计算、人头计算等 * " +
            "* @see com.alsc.saas.calculate.sdk.dto.enums.calculate.AdditionalCalculateTypeEnum " +
            "* CALCULATE_BY_FIXED(“CALCULATE_BY_FIXED”, “固定金额”)," +
            " * CALCULATE_BY_NUM(“CALCULATE_BY_NUM”, “按人数”)," +
            " * CALCULATE_BY_PERCENT(“CALCULATE_BY_PERCENT”, “按比例”)," +
            " * CALCULATE_BY_STEP(“CALCULATE_BY_STEP”, “阶梯计费”), " +
            "* CALCULATE_BY_COMPLEMENT_AMOUNT(“CALCULATE_BY_COMPLEMENT_AMOUNT”, “低消补差”)")
    private String extraCalType;

    @ApiModelProperty("附加费参与计算值，与上面的费用参与计算方式相关。 * 1、固定金额时，值相当于附加费总费用 extraTotalFee * 2、按人数时，传人数，查boh时会返回。 * 3、按比例时，传比例，查boh时会返回。")
    private String extraCalValue;

    @ApiModelProperty("参与分摊标，客侧餐盒费需要分摊到商品上。VALUE:true、false")
    private Boolean participateSplitFlag;

    @ApiModelProperty("参与优惠标，客侧附加费也可参与整单优惠。VALUE:true、false")
    private Boolean participateDiscountFlag;

    @ApiModelProperty("额外费用RuleId，用于核验规则。应该是BOH上附加费的规则ID")
    private String extraRuleId;
}
