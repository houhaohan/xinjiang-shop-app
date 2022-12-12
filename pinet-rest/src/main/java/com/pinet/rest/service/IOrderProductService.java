package com.pinet.rest.service;

import com.pinet.rest.entity.OrderProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.bo.OrderProductBo;

import java.util.List;

/**
 * <p>
 * 订单商品表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-08
 */
public interface IOrderProductService extends IService<OrderProduct> {

    /**
     * 根据订单id查找订单商品
     * @param orderId 订单id
     * @return List
     */
    List<OrderProductBo> getOrderProduct(Long orderId);

}
