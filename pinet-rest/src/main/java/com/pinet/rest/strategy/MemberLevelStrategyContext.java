package com.pinet.rest.strategy;

import com.pinet.rest.entity.enums.MemberLevelEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 会员策略上下文
 * @author chengshuanghui
 */
public class MemberLevelStrategyContext {

    private BigDecimal orderPrice;

    public MemberLevelStrategyContext(BigDecimal orderPrice){
        this.orderPrice = orderPrice;
    }

    public MemberLevelStrategy creat(Integer level){
        if(Objects.equals(level, MemberLevelEnum._0.getCode())){
            return new MkStrategy();
        }else if(Objects.equals(level,MemberLevelEnum._10.getCode())){
            return new VipStrategy();
        }else if(Objects.equals(level,MemberLevelEnum._20.getCode())){
            return new DbzStrategy();
        }
        return new NomalStrategy();
    }

    /**
     * 获取用户不同等级 积分比例
     * @param level
     * @return
     */
    public BigDecimal ratio(Integer level){
        return creat(level).ratio();
    }

    /**
     * 获取订单积分
     * @param level
     * @return
     */
    public Integer getScore(Integer level){
        return orderPrice.multiply(ratio(level))
                .setScale(0, RoundingMode.DOWN)
                .intValue();
    }
}
