package com.pinet.keruyun.openapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OrderDetailVO {

    @ApiModelProperty("主体信息")
    private OrderBaseVO orderBaseVO;

    @ApiModelProperty("桌台信息")
    private List<OrderTableVo> orderTableVoList;

    @ApiModelProperty("菜品明细")
    private List<OrderItemVo> orderItemVoList;

    @ApiModelProperty("订单优惠信息")
    private List<OpenOrderPromoVo> openOrderPromoVoList;

    @ApiModelProperty("服务费信息")
    private List<OpenExtraFeeVo> openExtraFeeVoList;

    @ApiModelProperty("支付信息")
    private List<OpenPaymentDetailVo> openPaymentDetailVoList;



    @Data
    public static class OrderBaseVO{
        @ApiModelProperty("门店ID")
        private String shopId;
        @ApiModelProperty("店铺名称")
        private String shopName;
        @ApiModelProperty("品牌ID")
        private String brandId;
        @ApiModelProperty("品牌名称")
        private String brandName;
        @ApiModelProperty("订单ID")
        private String orderId;
        @ApiModelProperty("业务订单号")
        private String busiOrderNo;
        @ApiModelProperty("三方订单号")
        private String thirdOrderNo;
        @ApiModelProperty("下单时间")
        private String openTime;
        @ApiModelProperty("结账时间")
        private String settleTime;
        @ApiModelProperty("营业日")
        private String finishBusiDate;
        @ApiModelProperty("订单来源")
        private String orderSource;
        @ApiModelProperty("订单类型")
        private String orderType;
        @ApiModelProperty("订单状态")
        private String orderStatus;
        @ApiModelProperty("订单金额")
        private Long orderAmt;
        @ApiModelProperty("优惠金额")
        private String promoAmt;
        @ApiModelProperty("订单收入")
        private String orderReceivedAmt;
        @ApiModelProperty("开单人")
        private String openOperatorName;
        @ApiModelProperty("结账人")
        private String settleOperatorName;
        @ApiModelProperty("会员ID")
        private String memberId;
        @ApiModelProperty("会员手机号")
        private String memberPhone;
        @ApiModelProperty("会员姓名")
        private String memberName;
        @ApiModelProperty("就餐人数")
        private String orderPeopleCnt;
        @ApiModelProperty("流水号")
        private String serialNo;
        @ApiModelProperty("整单备注")
        private String subject;
        @ApiModelProperty("关联订单ID，如整单退或反结时的原单订单ID")
        private String relatedOrderId;
        @ApiModelProperty("第三方订单流水号")
        private String thirdSerialNo;
    }

    @Data
    public static class OrderTableVo{
        @ApiModelProperty("桌台ID")
        private String tableId;
        @ApiModelProperty("桌台名称")
        private String tableName;
    }

    @Data
    public static class OrderItemVo{
        @ApiModelProperty("桌台名称")
        private String itemType;
        @ApiModelProperty("是否赠送")
        private Boolean giftFlag;
        @ApiModelProperty("是否是称重商品")
        private Boolean weighFlag;
        @ApiModelProperty("是否是临时菜")
        private Boolean tempFlag;
        @ApiModelProperty("是否是优惠菜")
        private Boolean promoFlag;
        @ApiModelProperty("商品大类名称")
        private String bigTypeName;
        @ApiModelProperty("商品中类名称")
        private String midTypeName;
        @ApiModelProperty("商品编码")
        private String itemCode;
        @ApiModelProperty("商品名称")
        private String itemName;
        @ApiModelProperty("商品售卖状态类型")
        private String saleStatusType;
        @ApiModelProperty("商品售卖状态类型编码")
        private String saleStatusTypeCode;
        @ApiModelProperty("商品id")
        private String id;
        @ApiModelProperty("父商品id")
        private String parentId;
        @ApiModelProperty("出品部门ID")
        private String productionDeptId;
        @ApiModelProperty("单位名称")
        private String unitName;
        @ApiModelProperty("规格")
        private String specName;
        @ApiModelProperty("规格名称全称")
        private String specNameConcat;
        @ApiModelProperty("商品原始单价")
        private String itemPrice;
        @ApiModelProperty("商品售价")
        private String salePrice;
        @ApiModelProperty("做法")
        private List<PracticeVo> practiceVoList;
        @ApiModelProperty("商品备注")
        private String itemSubject;
        @ApiModelProperty("销售数量、退菜数量或赠菜数量 1. 是否是赠菜通过giftFlag判断 2. 是否是退菜通过saleStatusTypeCode判断，DISCARD表示退菜，NORMAL表示销售")
        private String quantity;
        @ApiModelProperty("商品销售金额")
        private String itemSaleAmt;
        @ApiModelProperty("服务费分摊金额")
        private String extraFeeApportionAmt;
        @ApiModelProperty("商品优惠分摊")
        private String itemPromoApportionAmt;
        @ApiModelProperty("商品收入")
        private String itemReceivedAmt;
        @ApiModelProperty("子节点")
        private List<OrderItemVo> children;
    }

    @Data
    public static class PracticeVo{
        @ApiModelProperty("做法名称")
        private String practiceName;
        @ApiModelProperty("做法金额")
        private Long practiceAmt;
    }

    @Data
    public static class OpenOrderPromoVo{
        @ApiModelProperty("优惠名称")
        private String promoName;
        @ApiModelProperty("优惠类型")
        private String promoType;
        @ApiModelProperty("优惠时间")
        private String promoTime;
        @ApiModelProperty("操作人姓名")
        private String operatorName;
        @ApiModelProperty("优惠金额")
        private String promoAmt;
    }

    @Data
    public static class OpenExtraFeeVo{
        @ApiModelProperty("服务费名称")
        private String extraFeeName;
        @ApiModelProperty("服务费类型")
        private String extraFeeType;
        @ApiModelProperty("服务费金额")
        private Long extraFeeAmt;
        @ApiModelProperty("服务费优惠分摊金额")
        private Long extraFeePromoApportionAmt;
        @ApiModelProperty("服务费收入")
        private Long extraFeeReceivedAmt;
        @ApiModelProperty("被分摊金额")
        private Long apportionedAmt;
        @ApiModelProperty("服务费优惠总金额")
        private Long promoTotalAmt;
    }

    @Data
    public static class OpenPaymentDetailVo{
        @ApiModelProperty("支付单号")
        private String payDetailNo;
        @ApiModelProperty("面额")
        private String faceAmt;
        @ApiModelProperty("支付金额")
        private String payAmt;
        @ApiModelProperty("商户优惠金额")
        private String shopPromoAmt;
        @ApiModelProperty("平台抽佣/服务费")
        private String platformServiceAmt;
        @ApiModelProperty("商户实收")
        private String actualReceiveAmt;
        @ApiModelProperty("支付方式名称")
        private String payMethodName;
        @ApiModelProperty("支付状态")
        private String payDetailStatus;
        @ApiModelProperty("创建时间/支出开始时间")
        private String payDetailStartTime;
        @ApiModelProperty("支付/退款完成时间")
        private String payDetailEndTime;
        @ApiModelProperty("支付优惠")
        private String payPromoAmt;
        @ApiModelProperty("券数量")
        private String couponCnt;
        @ApiModelProperty("券名称")
        private String couponName;
        @ApiModelProperty("操作人姓名/收银员")
        private String operatorName;
        @ApiModelProperty("实际支付金额")
        private String actualPayAmt;
        @ApiModelProperty("平台优惠金额")
        private String platformPromoAmt;
        @ApiModelProperty("支付方式ID，-3:现金，-4:银行卡，-129:扫码支付，-130:收银码，-5:微信，-6:支付宝，-37:云闪付，-1:会员卡，-15:实体卡，-20:匿名卡，-127:储值补录，-128:挂账，-24:美团团购券，-36:口碑团购券，0:抵用券")
        private String payMethodId;
        @ApiModelProperty("团购券是否已对账 true:已对账 false:未对账")
        private String couponReconcileFlag;


    }
}
