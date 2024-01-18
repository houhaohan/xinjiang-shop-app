package com.pinet.rest.service;

import com.pinet.rest.entity.CouponProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 优惠券店商品表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-01-18
 */
public interface ICouponProductService extends IService<CouponProduct> {

    void getByProductIds(List<Long> prodIds);
}
