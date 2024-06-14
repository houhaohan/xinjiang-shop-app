package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.BigDecimalUtil;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.VipShopBalance;
import com.pinet.rest.entity.VipUser;
import com.pinet.rest.entity.enums.BalanceRecordTypeEnum;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.entity.param.OrderRefundNotifyParam;
import com.pinet.rest.entity.param.PayParam;
import com.pinet.rest.entity.param.RefundParam;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: xinjiang-shop-app
 * @description: 余额支付
 * @author: hhh
 * @create: 2023-09-12 14:05
 **/
@Service("balance_service")
@RequiredArgsConstructor
public class BalancePayServiceImpl implements IPayService {

    private final IOrdersService ordersService;
    private final ICustomerService customerService;
    private final ICustomerBalanceRecordService customerBalanceRecordService;
    private final IVipUserService vipUserService;
    private final IVipShopBalanceService vipShopBalanceService;
    private final JmsUtil jmsUtil;

    @Override
    @DSTransactional
    public Object pay(PayParam param) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        VipUser vipUser = vipUserService.getByCustomerId(customerId);
        if(vipUser == null){
            throw new PinetException("您的余额不足，请先充值");
        }
        VipShopBalance vipShopBalance = vipShopBalanceService.getByCustomerIdAndShopId(customerId,param.getShopId());
        if(vipShopBalance == null){
            throw new PinetException("您的余额不足，请先充值");
        }
        if (vipShopBalance.getAmount().compareTo(param.getPayPrice()) < 0) {
            throw new PinetException("您的余额不足，请先充值");
        }
        Customer customer = customerService.getById(customerId);
        //判断支付密码是否正确
        if(StringUtil.isBlank(customer.getPayPassword())){
            throw new PinetException("未设置支付密码  请先设置支付密码");
        }
        if (!customer.getPayPassword().equals(param.getPayPassWord())) {
            throw new PinetException("支付密码错误");
        }
        //更新余额
        vipShopBalance.setAmount(BigDecimalUtil.subtract(vipShopBalance.getAmount(),param.getPayPrice()));
        vipShopBalanceService.updateById(vipShopBalance);
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
        vipShopBalanceService.updateAmount(param.getCustomerId(),param.getShopId(),new BigDecimal(param.getRefundFee()));
        //插入资金流水
        customerBalanceRecordService.addCustomerBalanceRecord(param.getCustomerId(), new BigDecimal(param.getRefundFee()), BalanceRecordTypeEnum._7, param.getOrderRefundId());

        OrderRefundNotifyParam orderRefundNotifyParam = new OrderRefundNotifyParam();
        orderRefundNotifyParam.setRefundNo(Long.valueOf(param.getOutRefundNo()));
        orderRefundNotifyParam.setOutTradeNo("");
        ordersService.orderRefundNotify(orderRefundNotifyParam);
        //消息通知
        String msg = "会员订单已退款，订单号["+param.getOrderNo()+"]，请及时同步客如云余额。";
        jmsUtil.sendMsgQueue(QueueConstants.MESSAGE_SEND,msg);
    }
}
