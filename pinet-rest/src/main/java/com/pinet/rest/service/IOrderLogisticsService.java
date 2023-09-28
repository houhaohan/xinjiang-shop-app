package com.pinet.rest.service;

import com.pinet.rest.entity.OrderLogistics;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单配送信息表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-09-08
 */
public interface IOrderLogisticsService extends IService<OrderLogistics> {

    /**
     * 订单物流信息
     * @param orderId
     * @return
     */
    OrderLogistics getByOrderId(Long orderId);

}
