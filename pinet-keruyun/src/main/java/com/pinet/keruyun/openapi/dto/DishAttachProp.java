package com.pinet.keruyun.openapi.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DishAttachProp {

    @ApiModelProperty("外部业务行号，标识唯一的行")
    private String outAttachPropNo;

    @ApiModelProperty(value = "菜品附加属性ID",required = true)
    private String attachPropId;

    @ApiModelProperty(value = "附加属性类型（PRACTICE(“PRACTICE”, “做法”), FLAVOR(“FLAVOR”, “口味”), REMARK(“REMARK”, “备注”)）",required = true)
    private String attachPropType;

    @ApiModelProperty(value = "附加属性编码",required = true)
    private String attachPropCode;

    @ApiModelProperty(value = "附加属性名称",required = true)
    private String attachPropName;

    @ApiModelProperty(value = "附加属性价格",required = true)
    private Long price;

    @ApiModelProperty(value = "附加属性数量",required = true)
    private Integer quantity;

    @ApiModelProperty(value = "附加属性总金额",required = true)
    private Long totalFee;

    @ApiModelProperty(value = "附加属性优惠金额",required = true)
    private Long promoFee;

    @ApiModelProperty(value = "附加属性应付金额",required = true)
    private Long actualFee;
}
