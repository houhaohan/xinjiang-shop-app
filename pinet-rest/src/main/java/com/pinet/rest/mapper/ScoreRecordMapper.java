package com.pinet.rest.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pinet.rest.entity.ScoreRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 积分明细 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2023-12-22
 */
public interface ScoreRecordMapper extends BaseMapper<ScoreRecord> {

    Page<ScoreRecord> selectPageList(Page<ScoreRecord> page, @Param("customerId") Long customerId);
}
