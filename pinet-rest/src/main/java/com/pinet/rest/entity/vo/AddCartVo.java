package com.pinet.rest.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 添加购物车vo
 * @author: hhh
 * @create: 2022-12-28 13:43
 **/
@Data
@ApiModel(value = "AddCartVo",description = "添加购物车vo")
public class AddCartVo {
    private Integer prodNum;

    private BigDecimal price;


}
