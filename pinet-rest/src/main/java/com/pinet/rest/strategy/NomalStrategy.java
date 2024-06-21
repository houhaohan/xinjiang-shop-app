package com.pinet.rest.strategy;

import java.math.BigDecimal;

/**
 * 正常用户策略,在vip_user表中找不到的用户
 * @author chengshuanghui
 */
public class NomalStrategy implements VipLevelStrategy {
    @Override
    public BigDecimal ratio() {
        return BigDecimal.ZERO;
    }

}
