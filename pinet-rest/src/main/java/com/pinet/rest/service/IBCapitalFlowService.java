package com.pinet.rest.service;

import com.pinet.rest.entity.BCapitalFlow;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.enums.CapitalFlowStatusEnum;
import com.pinet.rest.entity.enums.CapitalFlowWayEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 商家资金流水表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-07-12
 */
public interface IBCapitalFlowService extends IService<BCapitalFlow> {

    /**
     * 添加流水
     * @param orderAmount 订单金额
     * @param orderId 订单id
     * @param orderTime  下单时间
     * @param capitalFlowWayEnum  支付方式
     * @param capitalFlowStatusEnum 状态
     * @param shopId 店铺id
     */
    void add(BigDecimal orderAmount, Long orderId, Date orderTime, CapitalFlowWayEnum capitalFlowWayEnum, CapitalFlowStatusEnum capitalFlowStatusEnum, Long shopId);
}
