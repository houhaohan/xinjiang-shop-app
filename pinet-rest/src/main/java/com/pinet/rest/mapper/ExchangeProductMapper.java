package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.rest.entity.ExchangeProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.dto.ExchangeProductListDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 兑换商品表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2024-01-16
 */
public interface ExchangeProductMapper extends BaseMapper<ExchangeProduct> {

    List<ExchangeProduct> selectExchangeProductList(Page<ExchangeProduct> page,@Param("dto") ExchangeProductListDto dto);
}
