package com.pinet.rest.strategy;

import java.math.BigDecimal;

/**
 * 门客（普通用户）策略
 * @author chengshuanghui
 */
public class MkStrategy implements VipLevelStrategy {

    public MkStrategy(){
    }


    @Override
    public BigDecimal ratio() {
        return BigDecimal.ONE;
    }

}
