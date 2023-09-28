package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.rest.entity.OrderLogistics;
import com.pinet.rest.mapper.OrderLogisticsMapper;
import com.pinet.rest.service.IOrderLogisticsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单配送信息表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-09-08
 */
@Service
public class OrderLogisticsServiceImpl extends ServiceImpl<OrderLogisticsMapper, OrderLogistics> implements IOrderLogisticsService {

    @Override
    public OrderLogistics getByOrderId(Long orderId) {
        QueryWrapper<OrderLogistics> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId);
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 1");
        return getOne(queryWrapper);
    }
}
