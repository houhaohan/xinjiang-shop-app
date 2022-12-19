package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.vo.OrderDetailVo;
import com.pinet.rest.entity.vo.OrderListVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface OrdersMapper extends BaseMapper<Orders> {

    Page<OrderListVo> selectOrderList(Page<OrderListVo> page , @Param("customerId") Long customerId);

    OrderDetailVo selectOrderDetail(@Param("orderId") Long orderId);

    Integer countShopOrderMakeNum(@Param("shopId") Long shopId,@Param("queryDate") Date queryDate);
}
