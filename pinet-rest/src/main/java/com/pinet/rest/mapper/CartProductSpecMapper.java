package com.pinet.rest.mapper;

import com.pinet.rest.entity.CartProductSpec;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 购物车商品样式表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-28
 */
public interface CartProductSpecMapper extends BaseMapper<CartProductSpec> {

    List<CartProductSpec> selectByCartId(@Param("cartId") Long cartId);

    CartProductSpec selectByUserIdAndSpecId(@Param("userId") Long userId,@Param("shopProdSpecId") Long shopProdSpecId);
}
