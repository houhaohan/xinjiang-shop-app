package com.pinet.rest.service;

import com.pinet.rest.entity.ProductType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.ProductTypeVo;

import java.util.List;


/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface IProductTypeService extends IService<ProductType> {

    /**
     * 根据店铺ID 查找商品分类
     * @param shopId
     * @return
     */
    List<ProductTypeVo> getByShopId(Long shopId);
}
