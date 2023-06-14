package com.pinet.rest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.dto.*;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.entity.param.OrderRefundNotifyParam;
import com.pinet.rest.entity.vo.*;

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
     * 统计会员中心数据
     * @param customerId 用户id
     * @return
     */
    MemberVo countMember(Long customerId);

    /**
     * 会员中心  推荐记录
     * @param dto
     * @return
     */
    List<RecommendListVo> recommendList(RecommendListDto dto);
}
