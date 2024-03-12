package com.pinet.rest.strategy;

import java.math.BigDecimal;

/**
 *
 * @author chengshuanghui
 */
public interface MemberLevelStrategy {

    /**
     * 积分比率
     * @return
     */
    BigDecimal ratio();


}
