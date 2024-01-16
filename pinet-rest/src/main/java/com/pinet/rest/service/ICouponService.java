package com.pinet.rest.service;

import com.pinet.rest.entity.Coupon;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 优惠券表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-10
 */
public interface ICouponService extends IService<Coupon> {

    /**
     * 根据customer_coupon表 主键id 查询优惠券
     * @param customerCouponId
     * @return
     */
    Coupon getByCustomerCouponId(Long customerCouponId);
}
