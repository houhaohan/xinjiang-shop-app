package com.pinet.rest.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: xinjiang-shop-app
 * @description: 兑换dto
 * @author: hhh
 * @create: 2024-01-16 15:02
 **/
@Data
public class ExchangeDto {
    @NotNull(message = "兑换商品id不能为空")
    private Long exchangeProductId;
}
