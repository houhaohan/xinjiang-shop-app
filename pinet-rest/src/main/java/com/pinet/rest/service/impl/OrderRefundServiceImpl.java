package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.rest.entity.OrderRefund;
import com.pinet.rest.mapper.OrderRefundMapper;
import com.pinet.rest.service.IOrderRefundService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单退款记录表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-01-04
 */
@Service
public class OrderRefundServiceImpl extends ServiceImpl<OrderRefundMapper, OrderRefund> implements IOrderRefundService {

    @Override
    public OrderRefund getByRefundNo(Long refundNo) {
        LambdaQueryWrapper<OrderRefund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderRefund::getRefundNo,refundNo);
        return getOne(queryWrapper);
    }
}
