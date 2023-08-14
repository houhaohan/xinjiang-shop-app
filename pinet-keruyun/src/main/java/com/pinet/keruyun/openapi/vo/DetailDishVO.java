package com.pinet.keruyun.openapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DetailDishVO {
    private String dishId;
    private String dishName;
    private String dishCode;
    private String dishType;
    private String categoryId;
    private List<DishSkuVO> dishSkuList;
    private String weighFlag;
    private Long weight;
    private String unitName;
    private String helpCode;
    private List<ComboGroupVO> comboGroupList;
    private String comboPriceIncludeChildDishSideDishPrice;
    private String comboPriceIncludeChildDishCookingWayPrice;
    private List<String> statsLabelNameList;
    private List<String> saleLabelNameList;
    private List<String> remarkNameList;
    private List<CookingWayGroupVO> cookingWayGroupList;
    private List<SideDishGroup> sideDishGroupList;
    private String state;
    private Integer startNumber;
    private Integer startInterval;
    private String modifyPriceFlag;
    private String discountFlag;
    private String orderSingleFlag;
    private List<String> dishImageUrlList;
    private Integer spicyLevel;
    private Integer sort;
    private List<DishStockInfo> dishStockInfoList;
    private String unitId;

    @Data
    private static class DishSkuVO{
        private String specName;
        private String defaultSkuFlag;
        private Long sellPrice;
        private String barCode;
        private String skuId;
        private String dishSkuCode;
    }

    @Data
    private static class ComboGroupVO{
        private String groupName;
        private Integer maxChoose;
        private Integer minChoose;
        private Integer sort;
        private String repeatable;
        private List<ComboGroupDetailVO> comboGroupDetailList;
        private String groupType;
    }

    @Data
    private static class ComboGroupDetailVO{
        private String singleDishId;
        private String maxChoose;
        private String minChoose;
        private String fixChoose;
        private String dishSkuId;
        private Long dishSkuPrice;
        private String optType;
        private String defaultFlag;
        private String dishName;
        private String specName;
        @ApiModelProperty("子菜售卖价，单位：分")
        private Long sellPrice;
        @ApiModelProperty("子菜是否为多规格 Y:是，N:否")
        private String multiSpecFlag;
        @ApiModelProperty("排序号")
        private Integer sort;
    }

    @Data
    private static class CookingWayGroupVO{
        private String cookingWayGroupName;
        private String optType;
        private List<CookingWayVO> cookingWayList;
        private String cookingWayGroupId;
    }

    @Data
    private static class CookingWayVO{
        private String cookingWayName;
        private Long addPrice;
        private String defaultFlag;
        private String cookingWayId;
    }

    @Data
    private static class SideDishGroup{
        @ApiModelProperty("分组名称")
        private String groupName;

        @ApiModelProperty("加料分组明细")
        private List<SideDishGroupDetail> sideDishGroupDetailList;
    }

    @Data
    private static class SideDishGroupDetail{
        private String sideDishName;
        private String sideDishId;
        private Long addPrice;
        private Integer sort;
    }

    @Data
    private static class DishStockInfo{
        @ApiModelProperty("菜品SPU ID")
        private String dishId;
        @ApiModelProperty("菜品SKU ID")
        private String skuId;
        @ApiModelProperty("剩余售卖量")
        private BigDecimal residualDecimal;
        @ApiModelProperty("是否售罄（Y：售罄，N：未售罄）")
        private String soldOutFlag;
    }
}
