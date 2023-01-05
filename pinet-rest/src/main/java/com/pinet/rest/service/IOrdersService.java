package com.pinet.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.dto.CreateOrderDto;
import com.pinet.rest.entity.dto.OrderListDto;
import com.pinet.rest.entity.dto.OrderPayDto;
import com.pinet.rest.entity.dto.OrderSettlementDto;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.entity.param.OrderRefundNotifyParam;
import com.pinet.rest.entity.vo.CreateOrderVo;
import com.pinet.rest.entity.vo.OrderDetailVo;
import com.pinet.rest.entity.vo.OrderListVo;
import com.pinet.rest.entity.vo.OrderSettlementVo;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-06
 */
public interface IOrdersService extends IService<Orders> {

    List<OrderListVo> orderList(OrderListDto dto);

    OrderDetailVo orderDetail(Long orderId);

    OrderSettlementVo orderSettlement(OrderSettlementDto dto);


    /**
     * 根据店铺id获取店铺制作中的数量
     * @param shopId 店铺id
     * @return Long
     */
    Integer countShopOrderMakeNum(Long shopId);

    CreateOrderVo createOrder(CreateOrderDto dto);

    Object orderPay(OrderPayDto dto);

    /**
     * 订单支付回调
     * @param param 回调需要的参数
     * @return Boolean
     */
    Boolean orderPayNotify(OrderPayNotifyParam param);

    /**
     * 取消订单
     * @param orderId 订单id
     * @return Boolean
     */
    Boolean cancelOrder(Long orderId);

    /**
     * 订单退款回调
     * @param param 退款回调参数
     * @return Boolean
     */
    Boolean orderRefundNotify(OrderRefundNotifyParam param);
}
