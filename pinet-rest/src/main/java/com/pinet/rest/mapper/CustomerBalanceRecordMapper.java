package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.rest.entity.CustomerBalanceRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.dto.BalanceRecordListDto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    List<CustomerBalanceRecord> selectListLimit5(@Param("customerId") Long customerId);

    Page<CustomerBalanceRecord> selectBalanceRecordList(Page<CustomerBalanceRecord> page, @Param("dto") BalanceRecordListDto dto);
}
