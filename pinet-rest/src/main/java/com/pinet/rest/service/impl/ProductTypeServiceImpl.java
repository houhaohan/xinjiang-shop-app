package com.pinet.rest.service.impl;

import com.pinet.rest.entity.ProductType;
import com.pinet.rest.entity.vo.ProductTypeVo;
import com.pinet.rest.mapper.ProductTypeMapper;
import com.pinet.rest.service.IProductTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


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


    @Override
    public List<ProductTypeVo> getByShopId(Long shopId) {
        return baseMapper.getByShopId(shopId);
    }
}
