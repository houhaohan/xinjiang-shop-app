package com.pinet.rest.service;

import com.pinet.rest.entity.CartComboDish;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.CartComboDishVo;

import java.util.List;

/**
 * <p>
 * 购物车套餐菜品表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-03-15
 */
public interface ICartComboDishService extends IService<CartComboDish> {

    /**
     * 删除购物车套餐明细
     * @param cartId
     */
    void deleteByCartId(Long cartId);

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
    List<CartComboDish> getByCartId(Long cartId);

    /**
     * 根据购物车ID查询
     * @param cartId
     * @param shopProdId 套餐ID
     * @return
     */
    List<CartComboDishVo> getComboDishByCartId(Long cartId,Long shopProdId);
}
