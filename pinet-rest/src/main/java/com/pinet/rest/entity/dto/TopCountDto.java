package com.pinet.rest.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: xinjiang-shop-app
 * @description: 我的页面上方统计
 * @author: hhh
 * @create: 2023-08-17 17:23
 **/
@Data
public class TopCountDto {
    private Integer couponCount;

    private BigDecimal balance = BigDecimal.ZERO;

    private Integer point = 0;
}
