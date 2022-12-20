package com.pinet.rest.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.rest.entity.ProductType;
import com.pinet.rest.entity.Shop;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.dto.ProductTypeDto;
import com.pinet.rest.entity.vo.ProductTypeVo;
import com.pinet.rest.mapper.ProductTypeMapper;
import com.pinet.rest.mapper.ShopProductMapper;
import com.pinet.rest.service.IProductTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.service.IShopService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Autowired
    private IShopService shopService;

    @Override
    public List<ProductTypeVo> productType(ProductTypeDto dto) {
        if(dto.getLat() == null && dto.getLng() == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //根据经纬度获取最近的店铺ID
        Shop shop = shopService.getMinDistanceShop(dto.getLat(), dto.getLng());
        if(shop == null){
            return Collections.emptyList();
        }
        //查点店铺商品
        List<ShopProduct> shopProductList = shopProductMapper.selectList(Wrappers.lambdaQuery(new ShopProduct())
                .eq(ShopProduct::getShopId, shop.getId())
        );
        //根据商品类型分类
        Map<Long, List<ShopProduct>> typeProductMap = null;
        if (ObjectUtil.isNotEmpty(shopProductList)){
            typeProductMap = shopProductList.stream().collect(Collectors.groupingBy(ShopProduct::getProductTypeId));
        }
        Set<Long> typeIds = typeProductMap.keySet();
        //查询商品类型
        List<ProductType> productTypeList = productTypeMapper.selectList(Wrappers.lambdaQuery(new ProductType())
                .eq(ProductType::getDelFlag, 0)
                .eq(ProductType::getTypeState, 0)
        );
        //转Vo
        List<ProductTypeVo> productTypeVoList = productTypeList.stream().map(productType -> {
            ProductTypeVo productTypeVo = new ProductTypeVo();
            BeanUtils.copyProperties(productType,productTypeVo);
            return productTypeVo;
        }).collect(Collectors.toList());

        for (ProductTypeVo productTypeVo : productTypeVoList) {
            for (Long id : typeIds) {
                if (id.equals(productTypeVo.getId())){
                    productTypeVo.setShopList(typeProductMap.get(id));
                }
            }
        }
        return productTypeVoList;
    }
}
