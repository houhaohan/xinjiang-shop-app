package com.pinet.rest.mapper;

import com.pinet.rest.entity.CartComboDish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.CartComboDishVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 购物车套餐菜品表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2024-03-15
 */
public interface CartComboDishMapper extends BaseMapper<CartComboDish> {

    /**
     * 查询购物车套餐明细
     * @param cartId
     * @param shopProdId 套餐ID
     * @return
     */
    List<CartComboDishVo> getComboDishByCartId(@Param("cartId") Long cartId,@Param("shopProdId") Long shopProdId);
}
