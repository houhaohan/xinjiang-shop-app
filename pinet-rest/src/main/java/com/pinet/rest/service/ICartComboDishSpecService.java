package com.pinet.rest.service;

import com.pinet.rest.entity.CartComboDishSpec;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.CartComboDishSpecVo;

import java.util.List;

/**
 * <p>
 * 购物车套餐菜品样式表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-03-15
 */
public interface ICartComboDishSpecService extends IService<CartComboDishSpec> {

    /**
     * 根据用户ID 和样式ID查询
     * @param userId
     * @param shopProdSpecIds
     * @return
     */
    List<CartComboDishSpecVo> getByUserIdAndShopProdSpecId(Long userId, List<Long> shopProdSpecIds);

    /**
     * 删除购物车套餐明细
     * @param cartId
     */
     void deleteByCartId(Integer cartId);

    /**
     * 批量删除购物车套餐明细
     * @param cartIds
     */
    void deleteByCartIds(List<Long> cartIds);

    /**
     * 根据购物车ID查询
     * @param cartId
     * @return
     */
    List<CartComboDishSpec> getByCartId(Long cartId);

    /**
     * 根据购物车ID查询
     * @param cartId
     * @param shopProdId
     * @return
     */
    List<CartComboDishSpec> getByCartIdAndProdId(Long cartId,Long shopProdId);
}
