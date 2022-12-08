package com.pinet.rest.mapper;

import com.pinet.rest.entity.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.vo.CartListVo;

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
}
