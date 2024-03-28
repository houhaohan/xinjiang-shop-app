package com.pinet.rest.mq.consumer;

/**
 * @description: 推送订单到客如云
 * @author: chengshuanghui
 * @date: 2024-03-28 17:32
 */

import com.imdada.open.platform.exception.RpcException;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.entity.enums.OrderTypeEnum;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.IDaDaService;
import com.pinet.rest.service.IOrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;


/**
 * @description: 客如云订单
 * @author: chengshuanghui
 * @date: 2024-03-28 10:54
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KryOrderPushListener {
    private final IOrdersService ordersService;
    private final JmsUtil jmsUtil;
    private final IDaDaService daDaService;

    /**
     *  推送订单到客如云
     * @param message orderId
     */
    @JmsListener(destination = QueueConstants.KRY_ORDER_PUSH, containerFactory = "queueListener")
    @Transactional(rollbackFor = Exception.class)
    public void orderPush(String message) {
        Orders order = ordersService.getById(Long.parseLong(message));
        //已推送的订单不用再推了
        if(StringUtil.isNotBlank(order.getMealCode()) || StringUtil.isNotBlank(order.getKryOrderNo())){
            return;
        }
        Orders entity = new Orders();
        entity.setId(order.getId());
        if(Objects.equals(order.getOrderType(), OrderTypeEnum.SELF_PICKUP.getCode())){
            //自提单
            String kryOrderNo = ordersService.scanCodePrePlaceOrder(order);
            entity.setKryOrderNo(kryOrderNo);
            entity.setOrderStatus(OrderStatusEnum.COMPLETE.getCode());
            //判断订单是否有佣金 如果有佣金 && 订单状态是已完成 设置佣金三天后到账
            if (BigDecimalUtil.gt(order.getCommission(), BigDecimal.ZERO)) {
                jmsUtil.delaySend(QueueConstants.QING_SHI_ORDER_COMMISSION, order.getId().toString(), 3 * 24 * 60 * 60 * 1000L);
            }
        }else {
            //外卖单
            String kryOrderNo = ordersService.takeoutOrderCreate(order);
            entity.setOrderStatus(OrderStatusEnum.SEND_OUT.getCode());
            entity.setKryOrderNo(kryOrderNo);
            entity.setOrderStatus(OrderStatusEnum.PAY_COMPLETE.getCode());
            //创建配送订单
            try {
                daDaService.createOrder(order);
            } catch (RpcException e) {
                //todo 短信提醒 15868805739
                jmsUtil.sendMsgQueue(QueueConstants.DELIVERY_ORDER_FAIL_SMS, order.getId().toString());
            }
        }
        ordersService.updateById(entity);
    }

}
