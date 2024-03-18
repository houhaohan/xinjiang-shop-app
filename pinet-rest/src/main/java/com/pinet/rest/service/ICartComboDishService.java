package com.pinet.rest.service;

import com.pinet.rest.entity.CartComboDish;
import com.baomidou.mybatisplus.extension.service.IService;

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
    void deleteByCartId(Integer cartId);

    /**
     * 批量删除购物车套餐明细
     * @param cartIds
     */
    void deleteByCartIds(List<Long> cartIds);
}
