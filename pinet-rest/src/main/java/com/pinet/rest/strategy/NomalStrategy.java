package com.pinet.rest.strategy;

import java.math.BigDecimal;

/**
 * 正常用户策略,在customer_member表中找不到的用户
 * @author chengshuanghui
 */
public class NomalStrategy implements MemberLevelStrategy{
    @Override
    public BigDecimal ratio() {
        return BigDecimal.ZERO;
    }

}
