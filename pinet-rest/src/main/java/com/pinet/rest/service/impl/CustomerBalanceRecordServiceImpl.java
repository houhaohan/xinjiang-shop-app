package com.pinet.rest.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.core.constants.DB;
import com.pinet.core.page.PageRequest;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.rest.entity.CustomerBalanceRecord;
import com.pinet.rest.entity.dto.BalanceRecordListDto;
import com.pinet.rest.entity.enums.BalanceRecordTypeEnum;
import com.pinet.rest.mapper.CustomerBalanceRecordMapper;
import com.pinet.rest.service.ICustomerBalanceRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        return sumMoneyByCustomerIdAndType(customerId,balanceRecordTypeEnum,null);
    }

    @Override
    public BigDecimal sumMoneyByCustomerIdAndType(Long customerId, BalanceRecordTypeEnum balanceRecordTypeEnum, Date startTime) {
        return baseMapper.sumMoneyByCustomerIdAndType(customerId,balanceRecordTypeEnum.getCode(),startTime);
    }

    @Override
    public List<CustomerBalanceRecord> getListLimit5(Long customerId) {
        List<CustomerBalanceRecord> customerBalanceRecords =  baseMapper.selectListLimit5(customerId);
        setTypeStr(customerBalanceRecords);
        return customerBalanceRecords;
    }

    @Override
    public List<CustomerBalanceRecord> balanceRecordList(BalanceRecordListDto dto) {
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        Page<CustomerBalanceRecord> page = new Page<>(dto.getPageNum(),dto.getPageSize());
        dto.setCustomerId(customerId);
        Page<CustomerBalanceRecord> pageList = baseMapper.selectBalanceRecordList(page,dto);
        setTypeStr(pageList.getRecords());
        return pageList.getRecords();
    }


    private void setTypeStr(List<CustomerBalanceRecord> customerBalanceRecords){
        customerBalanceRecords.forEach(k->{
            k.setTypeStr(BalanceRecordTypeEnum.getEnumByCode(k.getType()));
        });
    }
}
