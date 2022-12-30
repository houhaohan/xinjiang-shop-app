package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.CustomerAddress;
import com.pinet.rest.entity.Shop;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "ShopProductListVo对象", description = "商品列表")
public class ShopProductListVo {

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺地址")
    private String address;

    @ApiModelProperty("距离，单位米")
    private BigDecimal distance;

    @ApiModelProperty("纬度")
    private String lat;

    @ApiModelProperty("经度")
    private String lng;

    @ApiModelProperty("购物车价格")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @ApiModelProperty("购物车商品数量")
    private Integer prodNum = 0;

    @ApiModelProperty("商品类型")
    private List<ProdTypeVo> typeList = new ArrayList<>();

    @ApiModelProperty("默认地址")
    private CustomerAddress defaultAddress;

    @ApiModelProperty("店铺信息")
    private Shop shopInfo;
}
