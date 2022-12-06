package com.pinet.rest.service.impl;

import com.pinet.rest.entity.ProductSku;
import com.pinet.rest.mapper.ProductSkuMapper;
import com.pinet.rest.service.IProductSkuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品规格表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Service
public class ProductSkuServiceImpl extends ServiceImpl<ProductSkuMapper, ProductSku> implements IProductSkuService {

}
