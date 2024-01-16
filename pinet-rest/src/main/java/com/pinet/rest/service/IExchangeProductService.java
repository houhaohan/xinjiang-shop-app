package com.pinet.rest.service;

import com.pinet.rest.entity.ExchangeProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.dto.ExchangeProductListDto;

import java.util.List;

/**
 * <p>
 * 兑换商品表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-16
 */
public interface IExchangeProductService extends IService<ExchangeProduct> {

    List<ExchangeProduct> exchangeProductList(ExchangeProductListDto dto);

    ExchangeProduct exchangeProductDetail(Long id);
}
