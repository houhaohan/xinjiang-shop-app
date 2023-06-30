package com.pinet.rest.service;

import com.pinet.rest.entity.CustomerBalanceRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pinet.rest.entity.enums.BalanceRecordTypeEnum;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 资金流水表 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
public interface ICustomerBalanceRecordService extends IService<CustomerBalanceRecord> {
    /**
     * 添加资金流水
     * @param customerId 用户id
     * @param money 金额
     * @param balanceRecordTypeEnum 类型
     * @param fkId 关联的外键id
     * @return
     */
    boolean addCustomerBalanceRecord(Long customerId, BigDecimal money, BalanceRecordTypeEnum balanceRecordTypeEnum, Long fkId);

    /**
     *根据type统计金额
     * @param customerId 用户id
     * @param balanceRecordTypeEnum 类型
     * @return
     */
    BigDecimal sumMoneyByCustomerIdAndType(Long customerId, BalanceRecordTypeEnum balanceRecordTypeEnum);

    /**
     * 根据type统计金额
     * @param customerId 用户id
     * @param balanceRecordTypeEnum 类型
     * @param startTime  >startTime
     * @return
     */
    BigDecimal sumMoneyByCustomerIdAndType(Long customerId, BalanceRecordTypeEnum balanceRecordTypeEnum, Date startTime);
}
