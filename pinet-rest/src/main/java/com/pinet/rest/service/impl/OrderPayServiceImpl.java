package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pinet.core.constants.DB;
import com.pinet.core.constants.OrderConstant;
import com.pinet.rest.entity.OrderPay;
import com.pinet.rest.entity.enums.PayTypeEnum;
import com.pinet.rest.mapper.OrderPayMapper;
import com.pinet.rest.service.IOrderPayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * 支付记录表 服务实现类
 * </p>
 *
 * @author chengshuanghui
 * @since 2022-12-20
 */
@Service
@DS(DB.MASTER)
public class OrderPayServiceImpl extends ServiceImpl<OrderPayMapper, OrderPay> implements IOrderPayService {
    @Resource
    private OrderPayMapper orderPayMapper;


    @Override
    public OrderPay getByOrderIdAndChannelId(Long orderId, String channelId) {
        return orderPayMapper.selectByOrderIdAndChannelId(orderId,channelId);
    }

    @Override
    public OrderPay getByOrderNo(Long orderNo) {
        LambdaQueryWrapper<OrderPay> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OrderPay::getOrderNo,orderNo);
        return getOne(lambdaQueryWrapper);
    }

    @Override
    public OrderPay getByOrderId(Long orderId) {
        LambdaQueryWrapper<OrderPay> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderPay::getOrderId,orderId);
        queryWrapper.orderByDesc(OrderPay::getId);
        queryWrapper.last(" limit 1 ");
        return getOne(queryWrapper);
    }

    @Override
    public BigDecimal getPaidPriceByCustomerId(Long customerId) {
        QueryWrapper<OrderPay> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("sum(pay_price)");
        queryWrapper.eq("customer_id",customerId);
        queryWrapper.eq("pay_type", PayTypeEnum.ORDER_PAY.getCode());
        queryWrapper.eq("pay_status", OrderConstant.PAID);
        BigDecimal totalPrice = getObj(queryWrapper, o -> new BigDecimal(o.toString()));
        return totalPrice == null ? BigDecimal.ZERO : totalPrice;
    }
}
