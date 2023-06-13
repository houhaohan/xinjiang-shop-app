package com.pinet.rest.service;

import com.pinet.rest.entity.CustomerMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.dto.PayDto;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
public interface ICustomerMemberService extends IService<CustomerMember> {

    /**
     * 店帮主充值
     * @param dto
     * @return
     */
    Object recharge(PayDto dto);

    /**
     * 根据用户id查询
     * @param customerId
     * @return
     */
    CustomerMember getByCustomerId(Long customerId);
}
