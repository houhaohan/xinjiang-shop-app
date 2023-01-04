package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.rest.entity.OrderPay;
import com.pinet.rest.mapper.OrderPayMapper;
import com.pinet.rest.service.IOrderPayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 支付记录表 服务实现类
 * </p>
 *
 * @author chengshuanghui
 * @since 2022-12-20
 */
@Service
public class OrderPayServiceImpl extends ServiceImpl<OrderPayMapper, OrderPay> implements IOrderPayService {
    @Resource
    private OrderPayMapper orderPayMapper;


    @Override
    public OrderPay getByOrderIdAndChannelId(Long orderId, String channelId) {
        return orderPayMapper.selectByOrderIdAndChannelId(orderId,channelId);
    }
}
