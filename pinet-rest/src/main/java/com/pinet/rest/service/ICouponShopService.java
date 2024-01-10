package com.pinet.rest.service;

import com.pinet.rest.entity.CouponShop;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 优惠券店铺表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-10
 */
public interface ICouponShopService extends IService<CouponShop> {

    /**
     * 判断优惠券是否属于该店铺
     * @param couponId
     * @param shopId
     * @return
     */
    boolean isExistsInShop(Long couponId,Long shopId);
}
