package com.pinet.rest.service.impl;

import com.pinet.rest.entity.Product;
import com.pinet.rest.entity.vo.ProductListVo;
import com.pinet.rest.mapper.ProductMapper;
import com.pinet.rest.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Override
    public List<ProductListVo> productList(Long shopId) {
        return baseMapper.selectProductList(shopId);
    }
}
