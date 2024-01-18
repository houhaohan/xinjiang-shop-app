package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.rest.entity.ExchangeRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.ExchangeRecordListVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 兑换记录 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2024-01-16
 */
public interface ExchangeRecordMapper extends BaseMapper<ExchangeRecord> {

    List<ExchangeRecordListVo> selectExchangeRecordList(Page<ExchangeRecordListVo> page,@Param("customerId") Long customerId);
}
