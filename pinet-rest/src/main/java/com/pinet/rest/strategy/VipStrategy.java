package com.pinet.rest.strategy;

import java.math.BigDecimal;

/**
 * 会员策略
 * @author chengshuanghui
 */
public class VipStrategy implements MemberLevelStrategy {



    public VipStrategy(){
    }



    @Override
    public BigDecimal ratio() {
        return BigDecimal.ONE;
    }

}
