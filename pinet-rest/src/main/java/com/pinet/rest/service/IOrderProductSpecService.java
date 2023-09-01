package com.pinet.rest.service;

import com.pinet.rest.entity.OrderProductSpec;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-29
 */
public interface IOrderProductSpecService extends IService<OrderProductSpec> {

    /**
     * 根据orderProdId查询
     * @param orderProdId
     * @return
     */
    List<OrderProductSpec> getByOrderProdId(Long orderProdId);
}
