package com.pinet.rest.service;

import com.pinet.rest.entity.CartSide;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.CartSideVO;

import java.util.List;

/**
 * <p>
 * 购物车加料 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-04-07
 */
public interface ICartSideService extends IService<CartSide> {

    /**
     * 根据购物车 ID删除
     * @param cartId
     * @return
     */
    boolean deleteByCartId(Long cartId);

    /**
     *  根据购物车 ID批量删除
     * @param cartIds
     * @return
     */
    boolean deleteByCartIds(List<Long> cartIds);

    /**
     * 查询购物车小料
     * @param cartId
     * @return
     */
    List<CartSideVO> getByCartId(Long cartId);


    /**
     * 根据用户ID和店铺ID查询
     * @param userId
     * @param shopId
     * @return
     */
    List<CartSideVO> getByUserIdAndShopId(Long userId,Long shopId);


}
