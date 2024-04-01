package com.pinet.rest.mapper;

import com.pinet.rest.entity.OrderDiscount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

/**
 * <p>
 * 订单优惠明细表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2023-08-22

 */
public interface OrderDiscountMapper extends BaseMapper<OrderDiscount> {

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertBatchSomeColumn(Collection<OrderDiscount> list);
}
