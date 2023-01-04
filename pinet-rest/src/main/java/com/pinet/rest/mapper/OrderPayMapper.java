package com.pinet.rest.mapper;

import com.pinet.rest.entity.OrderPay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 支付记录表 Mapper 接口
 * </p>
 *
 * @author chengshuanghui
 * @since 2022-12-20
 */
public interface OrderPayMapper extends BaseMapper<OrderPay> {

    OrderPay selectByOrderIdAndChannelId(@Param("orderId") Long orderId,@Param("channelId") String channelId);
}
