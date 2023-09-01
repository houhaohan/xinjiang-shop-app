package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@ToString
@Data
public class KrySnackOrderCreateDTO {
    @ApiModelProperty(value = "合作方订单标号", required = true)
    @NotNull
    private String tpOrderId;

    @ApiModelProperty(value = "合作方流水号")
    @Length(max = 13)
    private String tpSerialNumber;

    @ApiModelProperty(value = "订单创建时间", required = true)
    @NotNull
    private Long createTime;

    @ApiModelProperty(value = "订单更新时间", required = true)
    @NotNull
    private Long updateTime;

    @ApiModelProperty(value = "是否需要发票")
    private Integer needInvoice;

    @ApiModelProperty("发票抬头,若选择了开票,则发票抬头必填")
    private String invoiceTitle;

    @ApiModelProperty("纳税人识别号，需要发票时必传")
    private String taxpayerId;

    @ApiModelProperty("发票金额,若选择了开票,则发票金额必填 单位：分")
    private Integer invoiceAmount;

    @ApiModelProperty(value = "门店编码", required = true)
    @NotNull
    private Long shopIdenty;

    @ApiModelProperty(value = "门店名称", required = true)
    @NotNull
    private String shopName;

    @ApiModelProperty("订单备注")
    private String remark;

    @ApiModelProperty(value = "总价 单位：分", required = true)
    @NotNull
    @Min(0)
    private Integer totalPrice;

    @ApiModelProperty(value = "就餐人数", required = true)
    @NotNull
    @Min(0)
    private Integer peopleCount;

    @ApiModelProperty(value = "订单状态,目前只支持'已确认'", required = true)
    private Integer status;

    @ApiModelProperty(value = "是否打印")
    private Integer print;

    @NotNull
    @ApiModelProperty(value = "菜品信息", required = true)
    @Size(min = 1)
    @Valid
    private List<KryOrderProduct> products;

    @ApiModelProperty("对账信息")
    @NotNull
    @Valid
    private KryOrderPayment payment;

    @ApiModelProperty(value = "会员集合")
    @Valid
    private List<Customer> customers;

    @ApiModelProperty(value = "优惠明细集合")
    @Valid
    private List<DiscountDetail> discountDetails;

    @ApiModelProperty(value = "税费")
    @Valid
    private Integer taxAmount;

    @ApiModelProperty(value = "税费总和")
    @Valid
    private BigDecimal totalTaxes;

    @ApiModelProperty(value = "税费详情")
    @Valid
    private List<TaxDetailsDTO> taxDetails;

    @ApiModelProperty(value = "桌台信息")
    @Valid
    private List<KryOrderTableInfo> tables;

    @ApiModelProperty(value = "小费详情")
    private TipDetailDTO tipDetail;
}
