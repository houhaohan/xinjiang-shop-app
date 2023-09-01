package com.pinet.keruyun.openapi.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDishRequest {

    @ApiModelProperty("外部菜品业务标记，标识唯一行")
    private String outDishNo;

    @ApiModelProperty("菜品ID")
    private String dishId;

    @ApiModelProperty("菜品类型， SINGLE_DISH(“SINGLE_DISH”, “单菜”)," +
            " COMBO_DISH(“COMBO_DISH”, “套餐菜”)," +
            " COMBO_DETAIL(“COMBO_DETAIL”, “套餐明细”)," +
            " ADDITIONAL_ITEM(“ADDITIONAL_ITEM”, “附加产品”)," +
            " CRM_CRAD(“CRM_CRAD”, “crm卡商品”)," +
            " COUPON_PACKAGE(“COUPON_PACKAGE”, “券包商品”)")
    private String dishType;

    @ApiModelProperty("菜品编码")
    private String dishCode;

    @ApiModelProperty("菜品名称")
    private String dishName;

    @ApiModelProperty("菜品备注")
    private String dishRemark;

    @ApiModelProperty("菜品数量")
    private Integer dishQuantity;

    @ApiModelProperty("菜品价格")
    private Integer dishFee;

    @ApiModelProperty("菜品原价")
    private Integer dishOriginalFee;

    @ApiModelProperty("菜品总金额")
    private Integer totalFee;

    @ApiModelProperty("菜品优惠总金额")
    private Long promoFee;

    @ApiModelProperty("菜品应付金额")
    private Integer actualFee;

    @ApiModelProperty("餐盒费")
    private String packageFee;

    @ApiModelProperty("菜品规格ID")
    private String dishSkuId;

    @ApiModelProperty("菜品规格编码")
    private String dishSkuCode;

    @ApiModelProperty("菜品规格名称")
    private String dishSkuName;

    @ApiModelProperty("称重菜标识")
    private Boolean weightDishFlag;

    @ApiModelProperty("称重菜第二单位ID")
    private String secondSkuId;

    @ApiModelProperty("称重菜第二单位名称")
    private String secondSkuName;

    @ApiModelProperty("称重菜菜品计量")
    private BigDecimal secondSkuQuantity;

    @ApiModelProperty("称重菜价格，单位分")
    private Long secondSkuPrice;

    @ApiModelProperty("菜品单位ID")
    private String unitId;

    @ApiModelProperty("菜品单位名称")
    private String unitName;

    @ApiModelProperty("菜品单位编码")
    private String unitCode;

    @ApiModelProperty("附加项（加料、做法）列表")
    private List<DishAttachProp> dishAttachPropList;

    @ApiModelProperty("套餐明细或配料列表 * dishType=SINGLE时，存放配料 * dishType=COMBO时，存放套餐明细")
    private List<ScanCodeDish> dishList;

    @ApiModelProperty("菜品md5,如果不用来标识菜品变化，则保持和outDishNo一样 * 如果有业务使用，取决于自身的MD5逻辑。md5：自定义菜品是否变化")
    private String dishMd5;

    @ApiModelProperty("是否固定加料数量 value true/false")
    private Boolean isFixAdditionalItemQuantityFlag;

    @ApiModelProperty("菜品图片url")
    private String dishImgUrl;

    @ApiModelProperty("是否必选菜品，true和false")
    private Boolean required;

    @ApiModelProperty("是否打包")
    private Boolean isPack;

    @ApiModelProperty("是否赠送的菜")
    private Boolean dishGiftFlag;

    @ApiModelProperty("boh菜品域的原始菜品类型，" +
            " * @see com.alsc.pos.dish.prod.client.dto.enums.DishTypeEnum * " +
            "SINGLE(“SINGLE”, “单品”), " +
            "* COMBO(“COMBO”, “套餐”), " +
            "* SIDE(“SIDE”, “配料”), " +
            "* PACKING_BOX(“PACKING_BOX”, “打包盒”), " +
            "* MEAL_LOSS(“MEAL_LOSS”, “餐损”), " +
            "* TEMPORARY(“TEMPORARY”, “临时菜”), " +
            "* DELIVERY_COST(“DELIVERY_COST”, “配送费”), " +
            "* CRM_CARD(“CRM_CARD”, “卡商品”)")
    private String itemOriginType;
}
