package com.pinet.rest.service.impl;

import com.pinet.rest.entity.OrderComboDish;
import com.pinet.rest.entity.vo.OrderComboDishVo;
import com.pinet.rest.mapper.OrderComboDishMapper;
import com.pinet.rest.service.IOrderComboDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单套餐菜品表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-03-20
 */
@Service
@RequiredArgsConstructor
public class OrderComboDishServiceImpl extends ServiceImpl<OrderComboDishMapper, OrderComboDish> implements IOrderComboDishService {

    private final OrderComboDishMapper orderComboDishMapper;

    @Override
    public List<OrderComboDishVo> getByOrderIdAndShopProdId(Long orderId, Long shopProdId) {
        return orderComboDishMapper.getByOrderIdAndShopProdId(orderId,shopProdId);
    }
}
