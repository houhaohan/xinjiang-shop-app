package com.pinet.rest.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.core.exception.PinetException;
import com.pinet.rest.entity.ExchangeProduct;
import com.pinet.rest.entity.dto.ExchangeProductListDto;
import com.pinet.rest.mapper.ExchangeProductMapper;
import com.pinet.rest.service.IExchangeProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 兑换商品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-16
 */
@Service
public class ExchangeProductServiceImpl extends ServiceImpl<ExchangeProductMapper, ExchangeProduct> implements IExchangeProductService {

    @Override
    public List<ExchangeProduct> exchangeProductList(ExchangeProductListDto dto) {
        Page<ExchangeProduct> page = new Page<>(dto.getPageNum(),dto.getPageSize());
        return baseMapper.selectExchangeProductList(page,dto);
    }

    @Override
    public ExchangeProduct exchangeProductDetail(Long id) {
        ExchangeProduct exchangeProduct = getById(id);
        if (ObjectUtil.isNull(exchangeProduct)){
            throw new PinetException("兑换商品不存在");
        }

        return null;
    }
}
