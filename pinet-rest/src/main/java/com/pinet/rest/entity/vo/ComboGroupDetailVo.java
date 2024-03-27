package com.pinet.rest.entity.vo;

import com.pinet.rest.entity.KryComboGroupDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: chengshuanghui
 * @date: 2024-03-27 10:18
 */
@Data
public class ComboGroupDetailVo extends KryComboGroupDetail {

    @ApiModelProperty("商品样式ID")
    private Long shopProdSpecId;

    @ApiModelProperty("SKU ID")
    private Long skuId;

    @ApiModelProperty("SKU名称")
    private String skuName;
}
