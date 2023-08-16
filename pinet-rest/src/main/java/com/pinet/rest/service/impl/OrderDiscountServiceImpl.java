package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pinet.core.entity.BaseEntity;
import com.pinet.rest.entity.OrderDiscount;
import com.pinet.rest.mapper.OrderDiscountMapper;
import com.pinet.rest.service.IOrderDiscountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单优惠明细表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-08-15
 */
@Service
public class OrderDiscountServiceImpl extends ServiceImpl<OrderDiscountMapper, OrderDiscount> implements IOrderDiscountService {

    @Override
    public List<OrderDiscount> getByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderDiscount> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderDiscount::getOrderId,orderId).eq(BaseEntity::getDelFlag,0);
        return list(lambdaQueryWrapper);
    }
}
