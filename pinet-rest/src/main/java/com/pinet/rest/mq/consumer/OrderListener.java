package com.pinet.rest.mq.consumer;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.pinet.rest.entity.vo.OrderDetailVo;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.entity.OrderProduct;
import com.pinet.rest.entity.OrderProductSpec;
import com.pinet.rest.entity.Orders;
import com.pinet.rest.entity.enums.OrderStatusEnum;
import com.pinet.rest.service.IOrderProductService;
import com.pinet.rest.service.IOrdersService;
import com.pinet.rest.service.IShopProductSpecService;
import com.pinet.rest.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private IOrdersService ordersService;

    @Resource
    private IOrderProductService orderProductService;

    @Resource
    private IShopProductSpecService shopProductSpecService;

    @Resource
    private ISmsService smsService;


    /**
     * 订单到期未支付
     * @param message orderId
     */
    @JmsListener(destination = QueueConstants.QING_SHI_ORDER_PAY_NAME, containerFactory = "queueListener")
    @Transactional(rollbackFor = Exception.class)
    public void orderConsumer(String message) {
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~订单到期未支付,系统自动取消 ~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Orders order = ordersService.getById(Long.parseLong(message));
        //如果是待付款  更改状态为已取消
        if (order.getOrderStatus().equals(OrderStatusEnum.NOT_PAY.getCode())) {
            ordersService.cancelOrder(order.getId());

            //库存回滚
            List<OrderProduct> orderProductList = orderProductService.getByOrderId(order.getId());
            for (OrderProduct orderProduct : orderProductList) {
                for (OrderProductSpec orderProductSpec : orderProduct.getOrderProductSpecs()) {
                    int res = shopProductSpecService.addStock(orderProductSpec.getShopProdSpecId(), orderProduct.getProdNum());
                    if (res != 1){
                        log.error("订单id："+order.getId()+"----订单商品："+orderProduct.getProdName() + "库存回滚失败");
                    }
                }

            }
        }
    }


    private static final String PHONE = "17513651537";
//    private static final Long SHOP_ID = 22L;
    private static final Long SHOP_ID = 24L;
    /**
     * 发送短信的时间
     */
    private static final String START_TIME = "06:00";
    private static final String END_TIME = "10:00";
    /**
     * 保利店早上订单发送短信到手机号
     * @param message orders
     */
    @JmsListener(destination = QueueConstants.QING_ORDER_SEND_SMS_NAME, containerFactory = "queueListener")
    public void sendSmsMsg(String message){
        log.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~保利店早上订单发送短信到手机号 ~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        Orders order = JSONObject.parseObject(message,Orders.class);
        //判断店铺是否是保利店
        if (!SHOP_ID.equals(order.getShopId())){
            return;
        }
        //判断当前时间是否发送短信
        //判断当前时间是否执行
        Date startTime = DateUtil.parse(START_TIME, "HH:mm");
        Date endTime = DateUtil.parse(END_TIME, "HH:mm");
        Date now = DateUtil.parse(DateUtil.format(new Date(), "HH:mm"), "HH:mm");

        if (!DateUtil.isIn(now, startTime, endTime)) {
            return;
        }
        OrderDetailVo vo =  ordersService.orderDetail(order.getId());

        StringBuilder productMsg = new StringBuilder("");
        for (OrderProduct orderProduct : vo.getOrderProducts()) {
            productMsg.append(orderProduct.getProdName()).append(":").
                    append(orderProduct.getOrderProductSpecs().stream().map(OrderProductSpec::getProdSpecName).collect(Collectors.joining(",")))
                    .append(";");
        }

        //发送短信
        String msg = "有新的顾客下单,取单号:"+vo.getMealCode() + "。餐品信息为:" + productMsg;

        smsService.sendSmsMsg(PHONE,msg);

    }
}
