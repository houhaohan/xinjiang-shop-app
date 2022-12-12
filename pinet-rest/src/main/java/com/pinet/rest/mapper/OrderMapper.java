package com.pinet.rest.mapper;

import com.pinet.rest.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.OrderDetailVo;
import com.pinet.rest.entity.vo.OrderListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface OrderMapper extends BaseMapper<Order> {

    List<OrderListVo> selectOrderList(@Param("customerId") Long customerId);

    OrderDetailVo selectOrderDetail(@Param("orderId") Long orderId);
}
