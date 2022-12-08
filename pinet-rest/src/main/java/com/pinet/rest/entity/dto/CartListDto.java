package com.pinet.rest.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;


/**
 * @program: xinjiang-shop-app
 * @description: 购物车列表dto
 * @author: hhh
 * @create: 2022-12-06 14:54
 **/
@Data
@ApiModel(value = "CartListDto",description = "购物车列表dto")
public class CartListDto {

    @ApiModelProperty(value = "店铺id",name = "shopId")
    @NotNull(message = "店铺id不能为空")
    private Integer shopId;


    @ApiModelProperty(value = "用户id(内部封装参数，不需要传)",name = "customerId")
    private Integer customerId;
}
