package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.CustomerBalanceRecord;
import com.pinet.rest.entity.enums.BalanceRecordTypeEnum;
import com.pinet.rest.mapper.CustomerBalanceRecordMapper;
import com.pinet.rest.service.ICustomerBalanceRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 资金流水表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
@Service
@DS(DB.SLAVE)
public class CustomerBalanceRecordServiceImpl extends ServiceImpl<CustomerBalanceRecordMapper, CustomerBalanceRecord> implements ICustomerBalanceRecordService {
    @Override
    public boolean addCustomerBalanceRecord(Long customerId, BigDecimal money, BalanceRecordTypeEnum balanceRecordTypeEnum, Long fkId) {
        CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord();
        customerBalanceRecord.setCustomerId(customerId);
        customerBalanceRecord.setMoney(money);
        customerBalanceRecord.setType(balanceRecordTypeEnum.getCode());
        customerBalanceRecord.setFkId(fkId);
        return save(customerBalanceRecord);
    }

    @Override
    public BigDecimal sumMoneyByCustomerIdAndType(Long customerId, BalanceRecordTypeEnum balanceRecordTypeEnum) {
        return baseMapper.sumMoneyByCustomerIdAndType(customerId,balanceRecordTypeEnum.getCode());
    }
}
