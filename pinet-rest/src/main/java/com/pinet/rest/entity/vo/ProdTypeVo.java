package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
public class ProdTypeVo {

    @ApiModelProperty("商品类型id")
    private Long productTypeId;

    @ApiModelProperty("商品类型名称")
    private String productType;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺地址")
    private String address;

    @ApiModelProperty("是否启用 0启用 1禁用")
    private List<ShopProductVo> productList;


}
