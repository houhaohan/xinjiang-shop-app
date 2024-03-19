package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-19 10:27
 */
@Data
public class ComboDishSpecVo {
    @ApiModelProperty("商品数量")
    private Integer quantity = 1;

    @ApiModelProperty("商品")
    private Long shopProdId;

    @ApiModelProperty("套餐内单品ID")
    private Long singleDishId;

    @ApiModelProperty("套餐内单品名称")
    private String singleDishName;

    @ApiModelProperty("套餐内单品样式名称")
    private String comboDishSpecNames;

    @ApiModelProperty("套餐内单品样式")
    private List<OrderProductSpecVo> orderProductSpecs;

    public String getComboDishSpecNames() {
        if(CollectionUtils.isEmpty(orderProductSpecs)){
            return "";
        }
        return "["+orderProductSpecs.stream().map(OrderProductSpecVo::getProdSpecName).collect(Collectors.joining(","))+"]";
    }
}
