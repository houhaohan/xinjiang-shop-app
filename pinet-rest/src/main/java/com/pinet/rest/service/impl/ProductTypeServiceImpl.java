package com.pinet.rest.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pinet.rest.entity.ProductType;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.dto.ProductTypeDto;
import com.pinet.rest.mapper.ProductTypeMapper;
import com.pinet.rest.mapper.ShopProductMapper;
import com.pinet.rest.service.IProductTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements IProductTypeService {

    @Autowired
    private ShopProductMapper shopProductMapper;

    @Override
    public Map<String, List<ShopProduct>> productType(ProductTypeDto dto) {
        List<ShopProduct> shopProductList = shopProductMapper.selectList(Wrappers.lambdaQuery(new ShopProduct())
                .eq(ShopProduct::getShopId, dto.getShopId())
        );
        Map<String, List<ShopProduct>> typeProductMap = null;
        if (ObjectUtil.isNotEmpty(shopProductList)){
            typeProductMap = shopProductList.stream().collect(Collectors.groupingBy(ShopProduct::getProductType));
        }
        return typeProductMap;
    }
}
