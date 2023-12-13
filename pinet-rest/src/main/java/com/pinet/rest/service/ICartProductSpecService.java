package com.pinet.rest.service;

import com.pinet.rest.entity.CartProductSpec;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 购物车商品样式表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-28
 */
public interface ICartProductSpecService extends IService<CartProductSpec> {
    List<CartProductSpec> getByCartId(Long cartId);

    /**
     * 购物车套餐商品规格
     * @param cartId
     * @return
     */
    List<CartProductSpec> getComboByCartId(Long cartId);

    CartProductSpec getByUserIdAndSpecId(Long userId,Long shopProdSpecId);

}
