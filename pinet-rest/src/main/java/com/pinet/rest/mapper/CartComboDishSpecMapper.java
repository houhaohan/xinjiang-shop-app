package com.pinet.rest.mapper;

import com.pinet.rest.entity.CartComboDishSpec;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.CartComboDishSpecVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 购物车套餐菜品样式表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2024-03-15
 */
public interface CartComboDishSpecMapper extends BaseMapper<CartComboDishSpec> {

    /**
     * 根据用户ID和样式ID 查询
     * @param userId
     * @param shopProdSpecIds
     * @return
     */
    List<CartComboDishSpecVo> getByUserIdAndShopProdSpecId(@Param("userId") Long userId, @Param("shopProdSpecIds") List<Long> shopProdSpecIds);
}
