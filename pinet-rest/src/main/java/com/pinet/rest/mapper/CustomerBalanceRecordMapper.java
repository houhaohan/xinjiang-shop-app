package com.pinet.rest.mapper;

import com.pinet.rest.entity.CustomerBalanceRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 资金流水表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2023-06-13
 */
public interface CustomerBalanceRecordMapper extends BaseMapper<CustomerBalanceRecord> {

    BigDecimal sumMoneyByCustomerIdAndType(@Param("customerId") Long customerId, @Param("type") Integer type, @Param("startTime") Date startTime);
}
