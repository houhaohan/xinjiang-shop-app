package com.pinet.rest.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @program: xinjiang-shop-app
 * @description: 情况购物车dto
 * @author: hhh
 * @create: 2023-11-30 09:53
 **/
@Data
public class ClearCartDto {
    @NotNull
    private Long shopId;
}
