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
@ApiModel(value = "AddCartDto",description = "添加购物车dto")
public class AddCartDto {
    @ApiModelProperty(value = "店铺id",name = "shopId")
    private Long shopId;

    @NotNull(message = "店铺商品id不能为空")
    @ApiModelProperty(value = "店铺商品id",name = "shopProdId")
    private Long shopProdId;

    @NotNull(message = "数量不能为空")
    @Min(value = 0,message = "数量最小为0")
    @ApiModelProperty(value = "商品数量",name = "prodNum")
    private Integer prodNum;

    @ApiModelProperty(value = "商品单价",name = "price")
    private BigDecimal price;

    @ApiModelProperty("套餐明细")
    private List<AddCartDto> comboDetails;

    @ApiModelProperty(value = "商品样式id(多个逗号分割)",name = "shopProdSpecId")
    private String shopProdSpecIds;

    @ApiModelProperty(value = "内部参数不需要传",name = "customerId")
    private Long customerId;

    // 分享的商品加购物车需要传经纬度，用来茶最近店铺
    @ApiModelProperty("纬度")
    private BigDecimal lat;

    @ApiModelProperty("经度")
    private BigDecimal lng;

    @ApiModelProperty("是否是他人分享来的商品，0-否，1-是")
    private Integer shareFlag = 0;

}
