package com.pinet.rest.service;

import com.pinet.rest.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.dto.AddCartDto;
import com.pinet.rest.entity.dto.CartListDto;
import com.pinet.rest.entity.dto.EditCartProdNumDto;
import com.pinet.rest.entity.vo.AddCartVo;
import com.pinet.rest.entity.vo.CartListVo;
import com.pinet.rest.entity.vo.CartVo;

import java.util.List;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
public interface ICartService extends IService<Cart> {

    List<CartListVo> cartList(CartListDto dto);

    AddCartVo addCart(AddCartDto dto);

    Boolean editCartProdNum(EditCartProdNumDto dto);

    /**
     * 根据userId和shopId获取购物车商品信息
     * @param userId 用户id
     * @param shopId 商品id
     * @return List
     */
    List<Cart> getByUserIdAndShopId(Long userId,Long shopId);

    /**
     * 根据用户id和店铺id删除购物车
     * @param shopId
     * @param customerId
     */
    void delCartByShopId(Long shopId,Long customerId);

    /**
     * 根据用户ID 和 店铺Id 查询购物车商品数量和总价格
     * @param shopId
     * @param customerId
     * @return
     */
    CartVo getCartByUserIdAndShopId(Long shopId, Long customerId);
}
