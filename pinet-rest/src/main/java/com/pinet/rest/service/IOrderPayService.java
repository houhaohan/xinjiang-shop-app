package com.pinet.rest.service;

import com.pinet.rest.entity.OrderPay;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 支付记录表 服务类
 * </p>
 *
 * @author chengshuanghui
 * @since 2022-12-20
 */
public interface IOrderPayService extends IService<OrderPay> {
    OrderPay getByOrderIdAndChannelId(Long orderId,String channelId);

    /**
     * 根据orderNo查询
     * @param orderNo 订单编号
     */
    OrderPay getByOrderNo(Long orderNo);
}
