package com.pinet.rest.mapper;

import com.pinet.rest.entity.VipEquity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pinet.rest.entity.vo.VipEquityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * VIP权益表 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2024-06-11
 */
public interface VipEquityMapper extends BaseMapper<VipEquity> {

    List<VipEquityVO> equityList(@Param("level") Integer level);
}
