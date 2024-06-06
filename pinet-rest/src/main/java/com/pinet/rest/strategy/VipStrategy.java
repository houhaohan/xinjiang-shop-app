package com.pinet.rest.strategy;

import com.pinet.core.util.BigDecimalUtil;
import com.pinet.rest.entity.enums.VipLevelEnum;

import java.math.BigDecimal;

/**
 * 会员策略
 * @author chengshuanghui
 */
public class VipStrategy implements MemberLevelStrategy {

    private Integer level;

    public VipStrategy(){
    }

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

    public Double score(BigDecimal price){
        return BigDecimalUtil.multiply(price, this.ratio()).doubleValue();
    }

}
