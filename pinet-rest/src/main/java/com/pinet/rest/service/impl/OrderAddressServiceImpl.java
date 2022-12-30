package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.constants.DB;
import com.pinet.core.entity.BaseEntity;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.CustomerAddress;
import com.pinet.rest.entity.OrderAddress;
import com.pinet.rest.mapper.OrderAddressMapper;
import com.pinet.rest.service.ICustomerAddressService;
import com.pinet.rest.service.IOrderAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

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

    @Resource
    private ICustomerAddressService customerAddressService;

    @Override
    public OrderAddress getOrderAddress(Long orderId) {
        LambdaQueryWrapper<OrderAddress> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderAddress::getOrderId,orderId).eq(BaseEntity::getDelFlag,0);
        return getOne(lambdaQueryWrapper);
    }

    @Override
    @DS(DB.SLAVE)
    public OrderAddress createByCustomerAddressId(Long customerAddressId) {
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        Date now = new Date();
        CustomerAddress customerAddress = customerAddressService.getById(customerAddressId);
        OrderAddress orderAddress = new OrderAddress();
        BeanUtils.copyProperties(customerAddress,orderAddress);
        orderAddress.setId(null);
        orderAddress.setCreateBy(userId);
        orderAddress.setCreateTime(now);
        orderAddress.setUpdateBy(userId);
        orderAddress.setUpdateTime(now);
        orderAddress.setDelFlag(0);
        return orderAddress;
    }
}
