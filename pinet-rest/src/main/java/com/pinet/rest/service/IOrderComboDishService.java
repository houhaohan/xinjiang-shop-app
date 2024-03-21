package com.pinet.rest.service;

import com.pinet.rest.entity.OrderComboDish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单套餐菜品表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-03-20
 */
public interface IOrderComboDishService extends IService<OrderComboDish> {

    /**
     * 根据订单ID 和 套餐单品ID 查询
     * @param orderId
     * @param singleProdId
     * @return
     */
    OrderComboDish getByOrderIdAndSingleProdId(Long orderId,Long singleProdId);
}
