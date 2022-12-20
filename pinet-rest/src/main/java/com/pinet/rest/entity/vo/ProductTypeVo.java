package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.ShopProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: bj
 * @create: 2022/12/20 11:43
 */
@Data
public class ProductTypeVo {
    @ApiModelProperty("商品类型id")
    private Long id;

    @ApiModelProperty("商品类型名称")
    private String typeName;

    @ApiModelProperty("是否启用 0启用 1禁用")
    private Integer typeState;

    @ApiModelProperty("是否启用 0启用 1禁用")
    private List<ShopProduct> shopList;
}
