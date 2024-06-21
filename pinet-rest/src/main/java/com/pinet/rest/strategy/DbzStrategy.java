package com.pinet.rest.strategy;

import java.math.BigDecimal;

/**
 * 店帮主策略
 * @author chengshuanghui
 */
public class DbzStrategy implements VipLevelStrategy {

    public DbzStrategy(){
    }


    @Override
    public BigDecimal ratio() {
        return new BigDecimal("2");
    }

}
