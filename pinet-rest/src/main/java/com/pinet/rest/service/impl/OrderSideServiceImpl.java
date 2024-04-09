package com.pinet.rest.service.impl;

import com.pinet.rest.entity.OrderSide;
import com.pinet.rest.entity.vo.OrderSideVo;
import com.pinet.rest.mapper.OrderSideMapper;
import com.pinet.rest.service.IOrderSideService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单加料 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-04-08
 */
@Service
public class OrderSideServiceImpl extends ServiceImpl<OrderSideMapper, OrderSide> implements IOrderSideService {

    @Override
    public List<OrderSideVo> getByOrderId(Long orderId) {
        return baseMapper.getByOrderId(orderId);
    }
}
