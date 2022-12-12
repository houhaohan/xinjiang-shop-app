package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.core.entity.BaseEntity;
import com.pinet.rest.entity.OrderAddress;
import com.pinet.rest.mapper.OrderAddressMapper;
import com.pinet.rest.service.IOrderAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
@Service
public class OrderAddressServiceImpl extends ServiceImpl<OrderAddressMapper, OrderAddress> implements IOrderAddressService {
    @Resource
    private OrderAddressMapper orderAddressMapper;
    @Override
    public OrderAddress getOrderAddress(Long orderId) {
        LambdaQueryWrapper<OrderAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderAddress::getOrderId,orderId).eq(BaseEntity::getDelFlag,0);
        return getOne(lambdaQueryWrapper);
    }
}
