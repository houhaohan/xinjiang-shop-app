package com.pinet.rest.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.pinet.core.constants.DB;
import com.pinet.rest.entity.CustomerBalance;
import com.pinet.rest.mapper.CustomerBalanceMapper;
import com.pinet.rest.service.ICustomerBalanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 用户余额表 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
@Service
@DS(DB.SLAVE)
public class CustomerBalanceServiceImpl extends ServiceImpl<CustomerBalanceMapper, CustomerBalance> implements ICustomerBalanceService {

    @Override
    public boolean addAvailableBalance(Long customerId, BigDecimal availableBalance) {
        CustomerBalance customerBalance = getById(customerId);
        if (ObjectUtil.isNull(customerBalance)) {
            return false;
        }
        customerBalance.setAvailableBalance(customerBalance.getAvailableBalance().add(availableBalance));
        customerBalance.setBalance(customerBalance.getBalance().add(availableBalance));
        return updateById(customerBalance);
    }
}
