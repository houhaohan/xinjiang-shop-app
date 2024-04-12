package com.pinet.rest.service;

import com.pinet.rest.entity.OrderSide;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.vo.OrderSideVo;

import java.util.List;

/**
 * <p>
 * 订单加料 服务类
 * </p>
 *
 * @author wlbz
 * @since 2024-04-08
 */
public interface IOrderSideService extends IService<OrderSide> {

    /**
     * 根据订单ID查找小料
     * @param orderId
     * @return
     */
    List<OrderSideVo> getByOrderId(Long orderId);

    /**
     * 根据订单商品ID查询
     * @param orderProdId
     * @param shopId
     * @return
     */
    List<OrderSideVo> getByOrderProdIdAndShopId(Long orderProdId,Long shopId);
}
