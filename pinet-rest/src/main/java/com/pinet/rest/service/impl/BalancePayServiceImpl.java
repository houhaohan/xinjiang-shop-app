package com.pinet.rest.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.CustomerBalance;
import com.pinet.rest.entity.enums.BalanceRecordTypeEnum;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.param.RefundParam;
import com.pinet.rest.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: xinjiang-shop-app
 * @description: 余额支付
 * @author: hhh
 * @create: 2023-09-12 14:05
 **/
@Service("balance_service")
public class BalancePayServiceImpl implements IPayService {
    @Resource
    private IOrdersService ordersService;

    @Resource
    private ICustomerBalanceService customerBalanceService;

    @Resource
    private ICustomerService customerService;

    @Resource
    private ICustomerBalanceRecordService customerBalanceRecordService;

    @Override
    public Object pay(PayParam param) {

        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        CustomerBalance customerBalance = customerBalanceService.getByCustomerId(customerId);
        if (ObjectUtil.isNull(customerBalance)) {
            throw new PinetException("余额支付失败,用户资金初始化失败");
        }

        Customer customer = customerService.getById(customerId);
        //判断支付密码是否正确
        if (customer.getPayPassword() == null || StrUtil.isBlank(customer.getPayPassword())) {
            throw new PinetException("未设置支付密码  请先设置支付密码");
        }

        if (!customer.getPayPassword().equals(param.getPayPassWord())) {
            throw new PinetException("支付密码错误");
        }

        //判断余额是否充足
        if (customerBalance.getAvailableBalance().compareTo(param.getPayPrice()) < 0) {
            throw new PinetException("余额不足");
        }


        //扣减可用资金
        customerBalanceService.subtractAvailableBalance(customerId, param.getPayPrice());

        //插入资金流水
        customerBalanceRecordService.addCustomerBalanceRecord(customerId, param.getPayPrice().negate(), BalanceRecordTypeEnum._3, param.getOrderId());

        OrderPayNotifyParam orderPayNotifyParam = new OrderPayNotifyParam(Long.valueOf(param.getOrderNo()), new Date(), null, "balance");
        return ordersService.orderPayNotify(orderPayNotifyParam);

    }

    @Override
    public String getPayName() {
        return "余额支付";
    }

    @Override
    public void refund(RefundParam param) {
        //退回可用资金
        customerBalanceService.addAvailableBalance(param.getCustomerId(), new BigDecimal(param.getRefundFee()));

        //插入资金流水
        customerBalanceRecordService.addCustomerBalanceRecord(param.getCustomerId(), new BigDecimal(param.getRefundFee()), BalanceRecordTypeEnum._7, param.getOrderRefundId());
    }
}
