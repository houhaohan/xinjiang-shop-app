package com.pinet.rest.mapper;

import com.pinet.rest.entity.ProductType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.ProductTypeVo;

import java.util.List;

/**
 * <p>
 * 商品分类表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface ProductTypeMapper extends BaseMapper<ProductType> {

    /**
     * 根据店铺查找分类
     * @param shopId
     * @return
     */
    List<ProductTypeVo> getByShopId(Long shopId);
}
