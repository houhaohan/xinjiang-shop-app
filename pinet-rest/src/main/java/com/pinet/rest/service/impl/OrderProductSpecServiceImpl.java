package com.pinet.rest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pinet.rest.entity.OrderProductSpec;
import com.pinet.rest.mapper.OrderProductSpecMapper;
import com.pinet.rest.service.IOrderProductSpecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-29
 */
@Service
public class OrderProductSpecServiceImpl extends ServiceImpl<OrderProductSpecMapper, OrderProductSpec> implements IOrderProductSpecService {

    @Override
    public List<OrderProductSpec> getByOrderProdId(Long orderProdId) {
        QueryWrapper<OrderProductSpec> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_prod_id",orderProdId);
        return list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<OrderProductSpec> entityList) {
        return baseMapper.insertBatchSomeColumn(entityList) > 0;
    }
}
