package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 更多商品vo
 * @author: hhh
 * @create: 2023-06-29 09:32
 **/
@Data
public class ProductListVo {
    @ApiModelProperty("类型名称")
    private String typeName;

    @ApiModelProperty("类型id")
    private Long typeId;

    @ApiModelProperty("商品信息")
    private List<MemberRecommendProdVo> productList;
}
