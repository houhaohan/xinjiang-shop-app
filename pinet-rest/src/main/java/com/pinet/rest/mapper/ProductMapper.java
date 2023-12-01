package com.pinet.rest.mapper;

import com.pinet.rest.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.ProductListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface ProductMapper extends BaseMapper<Product> {

    List<ProductListVo> selectProductList(@Param("shopId") Long shopId);

}
