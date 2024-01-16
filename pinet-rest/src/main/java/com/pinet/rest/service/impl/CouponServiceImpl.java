package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.rest.entity.Coupon;
import com.pinet.rest.mapper.CouponMapper;
import com.pinet.rest.service.ICouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 优惠券表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-10
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {


    @Override
    public Coupon getByCustomerCouponId(Long customerCouponId) {
        QueryWrapper<Coupon> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("id = (select coupon_id from customer_coupon where del_flag = 0 and id = {0})",customerCouponId);
        return getOne(queryWrapper);
    }
}
