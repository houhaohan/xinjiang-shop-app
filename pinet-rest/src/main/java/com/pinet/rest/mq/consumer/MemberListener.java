package com.pinet.rest.mq.consumer;

import com.pinet.rest.entity.CustomerMember;
import com.pinet.rest.entity.enums.MemberLevelEnum;
import com.pinet.rest.mq.constants.QueueConstants;
import com.pinet.rest.service.ICustomerMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @program: xinjiang-shop-app
 * @description: 店帮主
 * @author: hhh
 * @create: 2023-06-30 15:03
 **/
@Component
@Slf4j
public class MemberListener {
    @Resource
    private ICustomerMemberService customerMemberService;


    /**
     * 店帮主到期自动设置为会员
     *
     * @param message
     */
    @JmsListener(destination = QueueConstants.QING_MEMBER_PAY_NAME, containerFactory = "queueListener")
    @Transactional(rollbackFor = Exception.class)
    public void memberConsumer(String message) {
        CustomerMember customerMember = customerMemberService.getByCustomerId(Long.valueOf(message));
        customerMember.setMemberLevel(MemberLevelEnum._10.getCode());
        customerMemberService.updateById(customerMember);
    }
}
