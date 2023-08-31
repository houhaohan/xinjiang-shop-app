package com.pinet.rest.service;

import com.pinet.rest.entity.OrderDiscount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 订单优惠明细表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-22
 */
public interface IOrderDiscountService extends IService<OrderDiscount> {

    /**
     * 根据订单ID查询
     * @param orderId
     * @return
     */
    List<OrderDiscount> getByOrderId(Long orderId);
}
