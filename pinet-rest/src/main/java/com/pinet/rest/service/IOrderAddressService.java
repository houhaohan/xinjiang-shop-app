package com.pinet.rest.service;

import com.pinet.rest.entity.OrderAddress;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface IOrderAddressService extends IService<OrderAddress> {
    /**
     * 根据订单id获取订单地址
     * @param orderId 订单id
     * @return OrderAddress
     */
    OrderAddress getOrderAddress(Long orderId);


    /**
     * 根据用户收货地址表构造订单收货地址信息
     * @param customerAddressId 用户收货地址表id
     * @return OrderAddress
     */
    OrderAddress createByCustomerAddressId(Long customerAddressId);
}
