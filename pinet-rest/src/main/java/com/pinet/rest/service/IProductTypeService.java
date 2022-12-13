package com.pinet.rest.service;

import com.pinet.rest.entity.ProductType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.ShopProduct;
import com.pinet.rest.entity.dto.ProductTypeDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface IProductTypeService extends IService<ProductType> {

    Map<String, List<ShopProduct>> productType(ProductTypeDto dto);
}
