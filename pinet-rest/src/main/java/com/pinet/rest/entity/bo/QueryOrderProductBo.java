package com.pinet.rest.entity.bo;

import com.pinet.rest.entity.dto.OrderComboDishDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 查询OrderProductbo
 * @author: hhh
 * @create: 2022-12-14 10:57
 **/
@Data
@AllArgsConstructor
public class QueryOrderProductBo {
    /**
     * 店铺商品id
     */
    private Long shopProdId;

    /**
     * 数量
     */
    private Integer prodNum;

    /**
     * 单品商品样式id
     */
    private List<Long> shopProdSpecIds;


    /**
     * 1外卖  2自提
     */
    private Integer orderType;

    @ApiModelProperty(value = "套餐菜明细")
    private List<OrderComboDishDto> orderComboDishList;


}
