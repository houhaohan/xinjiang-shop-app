package com.pinet.rest.service;

import com.pinet.rest.entity.OrderProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.bo.QueryOrderProductBo;
import com.pinet.rest.entity.dto.OrderProductDto;

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
     *
     * @param orderId 订单id
     * @return List
     */
    List<OrderProduct> getByOrderId(Long orderId);

    /**
     * 根据店铺信息查询购物车商品 校验数据并构造List<OrderProduct>原始数据
     * 下单结算时使用 会校验剩余库存  商品状态==
     *
     * @param shopId 店铺id
     * @return List
     */
    List<OrderProduct> getByCartAndShop(Long shopId,Integer orderType);

    /**
     * 通过QueryOrderProductBo参数构造OrderProduct数据
     * 下单结算时使用 会校验剩余库存  商品状态==
     *
     * @param queryOrderProductBo 查询param
     * @return OrderProduct
     */
    OrderProduct getByQueryOrderProductBo(QueryOrderProductBo queryOrderProductBo);

    /**
     * 根据订单id 查询商品信息，对接客如云订单 使用
     * @param orderId
     * @return
     */
    List<OrderProductDto> selectByOrderId(Long orderId);


}
