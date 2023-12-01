package com.pinet.rest.service;

import com.pinet.rest.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.ProductListVo;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface IProductService extends IService<Product> {
    /**
     * 查询所有商品 并按照类型分组
     * @return
     */
    List<ProductListVo> productList(Long shopId);
}
