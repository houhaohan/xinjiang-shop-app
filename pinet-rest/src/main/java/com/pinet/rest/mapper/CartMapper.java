package com.pinet.rest.mapper;

import com.pinet.rest.entity.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.vo.CartListVo;
import com.pinet.rest.entity.vo.CartVo;
import org.apache.ibatis.annotations.Param;

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
}
