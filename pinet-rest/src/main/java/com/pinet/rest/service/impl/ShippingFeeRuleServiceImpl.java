package com.pinet.rest.service.impl;

import com.pinet.rest.entity.ShippingFeeRule;
import com.pinet.rest.mapper.ShippingFeeRuleMapper;
import com.pinet.rest.service.IShippingFeeRuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 配送费规则 服务实现类
 * </p>
 *
 * @author wlbz
 * @since 2023-12-13
 */
@Service
public class ShippingFeeRuleServiceImpl extends ServiceImpl<ShippingFeeRuleMapper, ShippingFeeRule> implements IShippingFeeRuleService {

    @Override
    public BigDecimal getByDistance(double distance) {
        return baseMapper.selectByDistance(distance);
    }
}
