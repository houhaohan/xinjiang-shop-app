package com.pinet.rest.mapper;

import com.pinet.rest.entity.OrderComboDishSpec;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

/**
 * <p>
 * 订单套餐菜品样式表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2024-03-20
 */
public interface OrderComboDishSpecMapper extends BaseMapper<OrderComboDishSpec> {

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertBatchSomeColumn(Collection<OrderComboDishSpec> list);
}
