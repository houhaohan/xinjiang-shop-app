package com.pinet.rest.service.payNotify.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.pinet.common.mq.util.JmsUtil;
import com.pinet.rest.entity.CustomerMember;
import com.pinet.rest.entity.OrderPay;
import com.pinet.rest.entity.enums.BalanceRecordTypeEnum;
import com.pinet.rest.entity.enums.MemberLevelEnum;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.entity.vo.ProductListVo;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.ICustomerBalanceRecordService;
import com.pinet.rest.service.ICustomerBalanceService;
import com.pinet.rest.service.ICustomerMemberService;
import com.pinet.rest.service.IOrderPayService;
import com.pinet.rest.service.payNotify.IPayNotifyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @program: xinjiang-shop-app
 * @description: 店帮主充值回调
 * @author: hhh
 * @create: 2023-06-13 14:09
 **/
@Service("recharge_notify_service")
public class RechargeNotifyServiceImpl implements IPayNotifyService {
    @Resource
    private IOrderPayService orderPayService;

    @Resource
    private ICustomerBalanceService customerBalanceService;

    @Resource
    private ICustomerBalanceRecordService customerBalanceRecordService;

    @Resource
    private ICustomerMemberService customerMemberService;

    @Resource
    private JmsUtil jmsUtil;

    @Override
    @DSTransactional
    public boolean payNotify(OrderPayNotifyParam param) {
        OrderPay orderPay = orderPayService.getByOrderNo(param.getOrderNo());
        if (orderPay == null) {
            return false;
        }

        //更新order_pay表字段
        orderPay.setPayStatus(2);
        orderPay.setPayTime(param.getPayTime());
        orderPay.setOutTradeNo(param.getOutTradeNo());
        orderPayService.updateById(orderPay);

        //更新用户钱包表
        customerBalanceService.addAvailableBalance(orderPay.getCustomerId(), orderPay.getPayPrice());


        //插入流水记录表
        customerBalanceRecordService.addCustomerBalanceRecord(orderPay.getCustomerId(), orderPay.getPayPrice(), BalanceRecordTypeEnum._5, orderPay.getId());

        CustomerMember customerMember = customerMemberService.getByCustomerId(orderPay.getCustomerId());
        //判断是否已经是店帮主了
        if (ObjectUtil.isNotNull(customerMember) && customerMember.getMemberLevel().equals(MemberLevelEnum._20.getCode())){
            return true;
        }


        int memberLevel = 0;
        Date expireTime = null;
        //判断累计充值   >500会员  >2000店帮主
        BigDecimal countRechargePrice;
        //有过期时间 并过期  说明的他的店帮主已经到期了  要重新充值
        if (ObjectUtil.isNotNull(customerMember) && customerMember.getExpireTime().getTime() < System.currentTimeMillis()){
            countRechargePrice = customerBalanceRecordService.sumMoneyByCustomerIdAndType(orderPay.getCustomerId(),BalanceRecordTypeEnum._5,customerMember.getExpireTime());
        }else {
            countRechargePrice = customerBalanceRecordService.sumMoneyByCustomerIdAndType(orderPay.getCustomerId(),BalanceRecordTypeEnum._5);
        }


        if (countRechargePrice.compareTo(new BigDecimal("500")) >= 0){
            memberLevel = 10;
        }
        //首次成为店帮主后设置过期时间  mq延迟一年后自动设置变为会员
        if (countRechargePrice.compareTo(new BigDecimal("2000")) >= 0){
            memberLevel = 20;
            expireTime = DateUtil.offset(new Date(), DateField.YEAR,1);
            jmsUtil.delaySend(QueueConstants.QING_MEMBER_PAY_NAME, orderPay.getCustomerId().toString(), (long) (15 * 60 * 1000));
        }

        if (memberLevel > 0){
            if (customerMember == null){
                customerMember = new CustomerMember();
                customerMember.setCustomerId(orderPay.getCustomerId());
            }
            customerMember.setMemberLevel(memberLevel);
            customerMember.setExpireTime(expireTime);
            customerMemberService.saveOrUpdate(customerMember);
        }

        return true;
    }
}
