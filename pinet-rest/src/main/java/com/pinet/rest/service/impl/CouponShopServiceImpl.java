package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.rest.entity.CouponShop;
import com.pinet.rest.mapper.CouponShopMapper;
import com.pinet.rest.service.ICouponShopService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 优惠券店铺表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-10
 */
@Service
public class CouponShopServiceImpl extends ServiceImpl<CouponShopMapper, CouponShop> implements ICouponShopService {

    @Override
    public boolean isExistsInShop(Long couponId,Long shopId) {
        QueryWrapper<CouponShop> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("coupon_id",couponId);
        queryWrapper.eq("shop_id",shopId);
        return count(queryWrapper) > 0;
    }
}
