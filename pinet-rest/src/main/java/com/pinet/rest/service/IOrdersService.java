package com.pinet.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.keruyun.openapi.dto.OrderSyncDTO;
import com.pinet.keruyun.openapi.dto.PerformanceCallDTO;
import com.pinet.rest.entity.OrderDiscount;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.dto.*;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.entity.param.OrderRefundNotifyParam;
import com.pinet.rest.entity.vo.*;

import java.math.BigDecimal;
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

    /**
     * 再来一单
     * @param orderId
     */
    void recurOrder(Long orderId,Long customerId);


    /**
     * 自提兑换码
     * @return
     */
    List<PickUpListVo> pickUpList();

    String scanCodePrePlaceOrder(Orders orders);

    /**
     * 推送外卖单到客如云
     * @param orders
     * @return
     */
    String takeoutOrderCreate(Orders orders);

    /**
     * 客如云订单交易推送接口
     * @param dto
     */
    boolean syncOrderStatus(OrderSyncDTO dto);

    /**
     * 完成订单
     * @param orderId
     * @return
     */
    boolean completeOrder(Long orderId);

    /**
     * 履约叫号
     * @param dto
     */
    void performanceCall(PerformanceCallDTO dto);
}
