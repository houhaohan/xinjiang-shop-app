package com.pinet.rest.service.impl;

import com.pinet.rest.entity.OrderComboDish;
import com.pinet.rest.mapper.OrderComboDishMapper;
import com.pinet.rest.service.IOrderComboDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单套餐菜品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-03-20
 */
@Service
public class OrderComboDishServiceImpl extends ServiceImpl<OrderComboDishMapper, OrderComboDish> implements IOrderComboDishService {

    @Override
    public OrderComboDish getByOrderIdAndSingleProdId(Long orderId, Long singleProdId) {
        return null;
    }
}
