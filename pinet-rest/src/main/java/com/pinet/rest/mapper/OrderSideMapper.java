package com.pinet.rest.mapper;

import com.pinet.rest.entity.OrderSide;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.OrderSideVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单加料 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2024-04-08
 */
public interface OrderSideMapper extends BaseMapper<OrderSide> {

    /**
     * 根据订单ID查找小料
     * @param orderId
     * @return
     */
    List<OrderSideVo> getByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据订单商品ID查找小料
     * @param orderProdId
     * @param shopId
     * @return
     */
    List<OrderSideVo> getByOrderProdIdAndShopId(@Param("orderProdId") Long orderProdId,@Param("shopId") Long shopId);
}
