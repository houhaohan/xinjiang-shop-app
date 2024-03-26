package com.pinet.rest.mapper;

import com.pinet.rest.entity.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.vo.CartListVo;
import com.pinet.rest.entity.vo.CartVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
public interface CartMapper extends BaseMapper<Cart> {

    List<CartListVo> selectCartList(CartListDto dto);

    /**
     * 根据店铺Id 和用户id 查找购物车信息
     * @param shopId
     * @param customerId
     * @return
     */
    CartVo getCartByUserIdAndShopId(@Param("shopId") Long shopId, @Param("customerId") Long customerId);

    /**
     * 根据商品ID 查询用户购物车信息
     * @param customerId
     * @param shopProdId
     * @return
     */
    Cart getByUserIdAndShopProdId(@Param("customerId") Long customerId, @Param("shopProdId") Long shopProdId);

    /**
     * 查询购物车单品价格 数量
     * @param cartId
     * @return
     */
    BigDecimal getSingleByCartId(@Param("cartId") Long cartId);

    /**
     * 查询购物车单品价格 数量
     * @param cartId
     * @return
     */
    Long getComboByCartId(@Param("cartId") Long cartId);
}
