package com.pinet.rest.service.impl;

import com.pinet.rest.entity.OrderComboDishSpec;
import com.pinet.rest.mapper.OrderComboDishSpecMapper;
import com.pinet.rest.service.IOrderComboDishSpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * <p>
 * 订单套餐菜品样式表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2024-03-20
 */
@Service
public class OrderComboDishSpecServiceImpl extends ServiceImpl<OrderComboDishSpecMapper, OrderComboDishSpec> implements IOrderComboDishSpecService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<OrderComboDishSpec> entityList) {
        return  baseMapper.insertBatchSomeColumn(entityList) > 0;
    }
}
