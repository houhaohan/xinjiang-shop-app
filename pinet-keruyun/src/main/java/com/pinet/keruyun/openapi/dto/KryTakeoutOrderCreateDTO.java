package com.pinet.keruyun.openapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@ToString
@Data
public class KryTakeoutOrderCreateDTO {

    @ApiModelProperty(value = "合作方订单编号", required = true)
    @NotBlank
    private String tpOrderId;    //合作方订单编号

    @ApiModelProperty(value = "是否需要发票 1:是 2:否")
    private Integer needInvoice = 2;    //是否需要发票 1:是 2:否

    @ApiModelProperty("发票抬头，需要发票时必传")
    private String invoiceTitle;//发票抬头

    @ApiModelProperty("纳税人识别号，需要发票时必传")
    private String taxpayerId;//纳税人识别号

    @ApiModelProperty(value = "订单创建时间", required = true)
    @NotNull
    private Long createTime;    //订单创建时间

    @ApiModelProperty("订单备注")
    @Size(max = 100)
    private String remark;        //订单备注

    @ApiModelProperty(value = "就餐人数", required = true)
    @NotNull
    @Min(0)
    private Integer peopleCount;

    @NotNull
    @ApiModelProperty(value = "店铺信息", required = true)
    @Valid
    private KryShop shop;

    @NotNull
    @ApiModelProperty(value = "商品信息", required = true)
    @Size(min = 1)
    @Valid
    private List<KryOrderProduct> products;
    @NotNull
    @ApiModelProperty(value = "配送信息", required = true)
    @Valid
    private Delivery delivery;

    @NotNull
    @ApiModelProperty(value = "支付信息", required = true)
    @Valid
    private KryOrderPayment payment;

    @ApiModelProperty(value = "订单状态, 已接受订单会直接触发云打印")
    private Integer status;

    @ApiModelProperty(value = "会员集合")
    @Valid
    private List<Customer> customers;

    @ApiModelProperty(value = "优惠明细集合")
    @Valid
    private List<DiscountDetail> discountDetails;

    @ApiModelProperty(value = "是否打印")
    @Valid
    private Integer isPrint;

    @ApiModelProperty(value = "打印模版类型集合")
    @Valid
    private Integer[] printTemplateTypes;

    @ApiModelProperty(value = "税费 单位：分")
    @Valid
    @Min(0)
    private Integer taxAmount;

    @ApiModelProperty(value = "税费总和")
    @Valid
    private BigDecimal totalTaxes;

    @ApiModelProperty(value = "税费详情")
    @Valid
    private List<TaxDetailsDTO> taxDetails;

    @ApiModelProperty(value = "小费详情")
    @Valid
    private TipDetailDTO tipDetail;
}
