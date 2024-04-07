package com.pinet.rest.mapper;

import com.pinet.rest.entity.CartSide;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.CartSideVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 购物车加料 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2024-04-07
 */
public interface CartSideMapper extends BaseMapper<CartSide> {

    /**
     * 查询购物车小料
     * @param cartId
     * @return
     */
    List<CartSideVO> getByCartId(@Param("cartId") Long cartId);
}
