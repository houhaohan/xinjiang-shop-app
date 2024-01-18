package com.pinet.rest.service.impl;

import com.pinet.rest.entity.CouponProduct;
import com.pinet.rest.mapper.CouponProductMapper;
import com.pinet.rest.service.ICouponProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 优惠券店商品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-18
 */
@Service
public class CouponProductServiceImpl extends ServiceImpl<CouponProductMapper, CouponProduct> implements ICouponProductService {

    @Override
    public void getByProductIds(List<Long> prodIds) {

    }
}
