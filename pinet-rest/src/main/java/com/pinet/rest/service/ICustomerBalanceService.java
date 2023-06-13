package com.pinet.rest.service;

import com.pinet.rest.entity.CustomerBalance;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 用户余额表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
public interface ICustomerBalanceService extends IService<CustomerBalance> {
    /**
     * 增加可用金额
     * @param availableBalance 增加的可用金额
     * @return 是否成功
     */
    boolean addAvailableBalance(Long customerId, BigDecimal availableBalance);

}
