package com.pinet.rest.mq.consumer;

import com.pinet.common.mq.config.QueueConstants;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.service.IOrderProductService;
import com.pinet.rest.service.IOrdersService;
import com.pinet.rest.service.IShopProductSpecService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 订单消费者
 * @author: hhh
 * @create: 2022-12-15 13:39
 **/
@Component
@Slf4j
public class OrderListener {
    @Resource
    private IOrdersService orderService;

    @Resource
    private IOrderProductService orderProductService;

    @Resource
    private IShopProductSpecService shopProductSpecService;


    /**
     * 订单到期未支付
     * @param message orderId
     */
    @JmsListener(destination = QueueConstants.QING_SHI_ORDER_PAY_NAME, containerFactory = "queueListener")
    @Transactional(rollbackFor = Exception.class)
    public void orderConsumer(String message) {
        Orders order = orderService.getById(Long.parseLong(message));
        //如果是待付款  更改状态为已取消
        if (order.getOrderStatus().equals(OrderStatusEnum.NOT_PAY.getCode())) {
            order.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
            order.setUpdateTime(new Date());
            orderService.updateById(order);

            //库存回滚
            List<OrderProduct> orderProductList = orderProductService.getByOrderId(order.getId());
            for (OrderProduct orderProduct : orderProductList) {
                int res = shopProductSpecService.addStock(orderProduct.getShopProdSpecId(), orderProduct.getProdNum());
                if (res != 1){
                    log.error("订单id："+order.getId()+"----订单商品："+orderProduct.getProdName() + "库存回滚失败");
                }
            }
        }
    }
}
