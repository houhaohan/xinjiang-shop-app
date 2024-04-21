package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 添加购物车dto
 * @author: hhh
 * @create: 2022-12-08 11:42
 **/
@Data
@ApiModel(value = "AddCartDTO",description = "添加购物车dto")
public class AddCartDTO {
    @ApiModelProperty(value = "店铺id",name = "shopId")
    private Long shopId;

    @NotNull(message = "店铺商品id不能为空")
    @ApiModelProperty(value = "店铺商品id",name = "shopProdId",required = true)
    private Long shopProdId;

    @NotNull(message = "数量不能为空")
    @Min(value = 0,message = "数量最小为0")
    @ApiModelProperty(value = "商品数量",name = "prodNum",required = true)
    private Integer prodNum;

    @ApiModelProperty(value = "商品单价",name = "price")
    private BigDecimal price;

    @ApiModelProperty("套餐明细")
    private List<CartComboDishDTO> comboDetails;

    @ApiModelProperty(value = "商品样式id(多个逗号分割)",name = "shopProdSpecId")
    private String shopProdSpecIds;

    @ApiModelProperty(value = "内部参数不需要传",name = "customerId",hidden = true)
    private Long customerId;

    // 分享的商品加购物车需要传经纬度，用来茶最近店铺
    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("经度")
    private BigDecimal lng;

    @ApiModelProperty("是否是他人分享来的商品，0-否，1-是")
    private Integer shareFlag = 0;

    @ApiModelProperty(value = "加料明细")
    private List<SideDishGroupDTO> sideDishGroupList;

    @Data
    @ApiModel(description = "套餐明细")
    public static class CartComboDishDTO{
        @ApiModelProperty("套餐ID")
        private Long shopProdId;

        @ApiModelProperty("单品ID")
        private Long singleProdId;

        @ApiModelProperty("单品样式ID")
        private String shopProdSpecIds;

        @ApiModelProperty("单品样式明细")
        private List<CartComboDishSpecDTO> comboDetails;
    }

    @Data
    @ApiModel(description = "单品样式")
    public static class CartComboDishSpecDTO{
        @ApiModelProperty("加价")
        private BigDecimal addPrice;

        @ApiModelProperty("样式ID")
        private Long shopProdSpecId;

        @ApiModelProperty("样式名称")
        private String shopProdSpecName;

    }

}
