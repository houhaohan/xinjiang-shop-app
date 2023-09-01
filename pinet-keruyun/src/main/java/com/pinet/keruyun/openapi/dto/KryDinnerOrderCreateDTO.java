package com.pinet.keruyun.openapi.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class KryDinnerOrderCreateDTO {

    @ApiModelProperty("客如云门店ID")
    private Long shopIdenty;

    @ApiModelProperty("客如云门店名称")
    private String shopName;

    @ApiModelProperty("第三方订单号")
    private String tpOrderId;

    @ApiModelProperty("订单状态,订单状态为“已确认”则无需pos同意／拒绝，且会触发打印；否则需要pos进行同意／拒绝操作 1:未处理 2:已确认 默认1")
    private Integer status;

    @ApiModelProperty("商品种数")
    private Integer productCategorySize;

    @ApiModelProperty("菜品总额 单位：分")
    private Integer totalPrice;

    @ApiModelProperty("优惠总额 无优惠传0，有优惠时为正数 单位：分")
    private Integer discountAmount;

    @ApiModelProperty("顾客实付＝销售金额－优惠金额 单位：分")
    private Integer userFee;

    @ApiModelProperty("订单备注，不支持emoji")
    private String remark;

    @ApiModelProperty("订单创建时间")
    private Long createTime;

    @ApiModelProperty("就餐人数")
    private Integer peopleCount;

    @ApiModelProperty("是否打印 1:打印 0:不打印 默认1")
    private Integer print;

    @ApiModelProperty("商品信息")
    private List<DinnerDishProduct> products;

    @ApiModelProperty("桌台信息")
    private List<TableInfo> tables;

    @ApiModelProperty("顾客信息")
    private List<DinnerOrderMember> customers;

    @ApiModelProperty("优惠明细")
    private List<DiscountDetail> discountDetails;
}
