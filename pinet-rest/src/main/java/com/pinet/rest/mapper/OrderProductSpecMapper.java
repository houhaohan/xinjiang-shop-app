package com.pinet.rest.mapper;

import com.pinet.rest.entity.OrderProductSpec;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-29
 */
public interface OrderProductSpecMapper extends BaseMapper<OrderProductSpec> {

    /**
     * 批量插入
     * @param list
     * @return
     */
    int insertBatchSomeColumn(Collection<OrderProductSpec> list);
}
