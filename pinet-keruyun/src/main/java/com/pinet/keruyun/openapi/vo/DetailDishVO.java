package com.pinet.keruyun.openapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DetailDishVO {
    @ApiModelProperty("菜品ID")
    private String dishId;

    @ApiModelProperty("菜品名称")
    private String dishName;

    @ApiModelProperty("菜品编码")
    private String dishCode;

    @ApiModelProperty("菜品类型。SINGLE：单菜 ，COMBO：套餐， SIDE：配料")
    private String dishType;

    @ApiModelProperty("分类ID")
    private String categoryId;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("规格信息")
    private List<DishSkuVO> dishSkuList;

    @ApiModelProperty("是否为称重菜 Y:是；N:否")
    private String weighFlag;

    @ApiModelProperty("重量，单位：毫克")
    private Long weight;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("助记码")
    private String helpCode;

    @ApiModelProperty("菜品为套餐时的套餐分组信息")
    private List<ComboGroupVO> comboGroupList;

    @ApiModelProperty("套餐价格是否包含子菜加料价格， Y:是；N:否")
    private String comboPriceIncludeChildDishSideDishPrice;

    @ApiModelProperty("套餐价格是否包含子菜做法价格, Y:是；N:否")
    private String comboPriceIncludeChildDishCookingWayPrice;

    @ApiModelProperty("统计标签")
    private List<String> statsLabelNameList;

    @ApiModelProperty("销售标签")
    private List<String> saleLabelNameList;

    @ApiModelProperty("备注名称列表")
    private List<String> remarkNameList;

    @ApiModelProperty("做法组信息")
    private List<CookingWayGroupVO> cookingWayGroupList;

    @ApiModelProperty("加料组信息")
    private List<SideDishGroup> sideDishGroupList;

    @ApiModelProperty("售卖状态(ONLINE-售卖, PAUSE-停售)")
    private String state;

    @ApiModelProperty("起售数量")
    private Integer startNumber;

    @ApiModelProperty("增售数量")
    private Integer startInterval;

    @ApiModelProperty("是否可以手动改价")
    private String modifyPriceFlag;

    @ApiModelProperty("是否可以手动打折")
    private String discountFlag;

    @ApiModelProperty("是否可以单点")
    private String orderSingleFlag;

    @ApiModelProperty("菜品图片")
    private List<String> dishImageUrlList;

    @ApiModelProperty("辣度等级")
    private Integer spicyLevel;

    @ApiModelProperty("排序值")
    private Integer sort;

    @ApiModelProperty("库存信息")
    private List<DishStockInfo> dishStockInfoList;

    @ApiModelProperty("单位ID")
    private String unitId;

    @ApiModelProperty("菜品图片信息列表")
    private List<DishImageInfo> dishImageInfoList;

    @Data
    public static class DishSkuVO{
        @ApiModelProperty("规格名称")
        private String specName;

        @ApiModelProperty("是否为默认规格")
        private String defaultSkuFlag;

        @ApiModelProperty("售卖价，单位：分")
        private Long sellPrice;

        @ApiModelProperty("条形码")
        private String barCode;

        @ApiModelProperty("菜品SKU ID")
        private String skuId;

        @ApiModelProperty("菜品SKU Code")
        private String dishSkuCode;
    }

    @Data
    public static class ComboGroupVO{

        @ApiModelProperty("分组名称")
        private String groupName;

        @ApiModelProperty("套餐分组子菜的最大选择数")
        private Integer maxChoose;

        @ApiModelProperty("套餐分组子菜的最小选择数")
        private Integer minChoose;

        @ApiModelProperty("排序值")
        private Integer sort;

        @ApiModelProperty("是否可重复选 Y:是；N:否")
        private String repeatable;

        @ApiModelProperty("组明细")
        private List<ComboGroupDetailVO> comboGroupDetailList;

        @ApiModelProperty("套餐组类型（FIXED:固定，OPTIONAL:可选）")
        private String groupType;
    }

    @Data
    public static class ComboGroupDetailVO{
        @ApiModelProperty("子菜菜品ID")
        private String singleDishId;

        @ApiModelProperty("最大可选数量")
        private Integer maxChoose;

        @ApiModelProperty("最小可选数量")
        private Integer minChoose;

        @ApiModelProperty("固定数量")
        private Integer fixChoose;

        @ApiModelProperty("子菜SKU ID")
        private String dishSkuId;

        @ApiModelProperty("套餐分组为可选分组时的子菜加价金额,单位：分")
        private Long dishSkuPrice;

        @ApiModelProperty("可选类型(OPTIONAL-可选/REQUIRED-必选)")
        private String optType;

        @ApiModelProperty("是否为默认子菜")
        private String defaultFlag;

        @ApiModelProperty("子菜名称")
        private String dishName;

        @ApiModelProperty("子菜规格名")
        private String specName;

        @ApiModelProperty("子菜售卖价，单位：分")
        private Long sellPrice;

        @ApiModelProperty("子菜是否为多规格 Y:是，N:否")
        private String multiSpecFlag;

        @ApiModelProperty("排序号")
        private Integer sort;
    }

    @Data
    public static class CookingWayGroupVO{

        @ApiModelProperty("做法组名")
        private String cookingWayGroupName;

        @ApiModelProperty("可选类型(OPTIONAL-可选/REQUIRED-必选)")
        private String optType;

        @ApiModelProperty("做法列表")
        private List<CookingWayVO> cookingWayList;

        @ApiModelProperty("做法组ID")
        private String cookingWayGroupId;
    }

    @Data
    public static class CookingWayVO{
        @ApiModelProperty("做法名称")
        private String cookingWayName;

        @ApiModelProperty("加价，单位：分")
        private Long addPrice;

        @ApiModelProperty("是否为默认")
        private String defaultFlag;

        @ApiModelProperty("做法ID")
        private String cookingWayId;
    }

    @Data
    public static class SideDishGroup{
        @ApiModelProperty("分组名称")
        private String groupName;

        @ApiModelProperty("加料分组明细")
        private List<SideDishGroupDetail> sideDishGroupDetailList;
    }

    @Data
    public static class SideDishGroupDetail{
        @ApiModelProperty("加料名称")
        private String sideDishName;

        @ApiModelProperty("加料ID")
        private String sideDishId;

        @ApiModelProperty("配料价格 单位： 分")
        private Long addPrice;

        @ApiModelProperty("排序值")
        private Integer sort;
    }

    @Data
    public static class DishStockInfo{
        @ApiModelProperty("菜品SPU ID")
        private String dishId;

        @ApiModelProperty("菜品SKU ID")
        private String skuId;

        @ApiModelProperty("剩余售卖量")
        private BigDecimal residualDecimal;

        @ApiModelProperty("是否售罄（Y：售罄，N：未售罄）")
        private String soldOutFlag;
    }

    /**
     * 菜品图片信息
     */
    @Data
    public static class DishImageInfo{
        @ApiModelProperty("菜品原图")
        private String imageUrl;

        @ApiModelProperty("16:9图片")
        private String scale16Vs9Url;

        @ApiModelProperty("1:1图片")
        private String scale1Vs1Url;

        @ApiModelProperty("4:3图片")
        private String scale4Vs3Url;
    }
}
