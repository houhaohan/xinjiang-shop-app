package com.pinet.rest.service;

import com.pinet.rest.entity.ShippingFeeRule;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 配送费规则 服务类
 * </p>
 *
 * @author wlbz
 * @since 2023-12-13
 */
public interface IShippingFeeRuleService extends IService<ShippingFeeRule> {

    /**
     * 根据距离查询配送费
     * @param distance 米
     * @return 配送费
     */
    BigDecimal getByDistance(double distance);
}
