package com.pinet.rest.entity.dto;

import com.pinet.core.page.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * @program: xinjiang-shop-app
 * @description: 兑换商品列表dto
 * @author: hhh
 * @create: 2024-01-16 14:40
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ExchangeProductListDto extends PageRequest {
    @NotNull(message = "shopId不能为空")
    private Long shopId;
}
