package com.pinet.rest.entity.vo;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-03-19 14:18
 */
@Data
public class CartComboDishVo {

    @ApiModelProperty("商品数量")
    private Integer quantity = 1;

    @ApiModelProperty("套餐内单品ID")
    private Long shopProdId;

    @ApiModelProperty("套餐内单品名称")
    private String prodName;

    @ApiModelProperty("套餐内单品图片")
    private String productImg;

    @ApiModelProperty("套餐内单品样式")
    private List<CartComboDishSpecVo> comboDishSpecs;

    @ApiModelProperty("套餐内单品样式名称")
    private String comboDishSpecNames;

    public String getComboDishSpecNames(){
        if(CollectionUtils.isEmpty(comboDishSpecs)){
            return "";
        }
        return "["+comboDishSpecs.stream().map(CartComboDishSpecVo::getShopProdSpecName).collect(Collectors.joining(","))+"]";
    }



}
