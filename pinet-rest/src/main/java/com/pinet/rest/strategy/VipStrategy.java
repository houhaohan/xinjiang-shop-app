package com.pinet.rest.strategy;

import java.math.BigDecimal;

/**
 * 会员策略
 * @author chengshuanghui
 */
public class VipStrategy implements VipLevelStrategy {

    private Integer level;

    public VipStrategy(Integer level){
        this.level = level;
    }

    @Override
    public BigDecimal ratio() {
        BigDecimal ratio;
        switch (this.level){
            case 1:
                ratio = BigDecimal.ONE;
                break;
            case 2:
                ratio = new BigDecimal("1.1");
                break;
            case 3:
                ratio = new BigDecimal("1.2");
                break;
            case 4:
                ratio = new BigDecimal("1.3");
                break;
            case 5:
                ratio = new BigDecimal("1.4");
                break;
            default:
                ratio = BigDecimal.ONE;
        }
        return ratio;
    }

}
