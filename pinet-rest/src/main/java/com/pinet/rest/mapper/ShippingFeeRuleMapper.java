package com.pinet.rest.mapper;

import com.pinet.rest.entity.ShippingFeeRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 配送费规则 Mapper 接口
 * </p>
 *
 * @author wlbz
 * @since 2023-12-13
 */
public interface ShippingFeeRuleMapper extends BaseMapper<ShippingFeeRule> {

    /**
     * 根据距离查询配送费
     * @param distance 米
     * @return
     */
    BigDecimal selectByDistance(@Param("distance") double distance);
}
