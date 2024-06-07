package com.pinet.rest.strategy;

import java.math.BigDecimal;

/**
 * 会员策略上下文
 * @author chengshuanghui
 */
public class VipLevelStrategyContext {

    private BigDecimal orderPrice;

    public VipLevelStrategyContext(){
    }

    public VipLevelStrategyContext(BigDecimal orderPrice){
        this.orderPrice = orderPrice;
    }

    public VipLevelStrategy creat(Integer level){
//        if(Objects.equals(level, MemberLevelEnum._0.getCode())){
//            return new MkStrategy();
//        }else if(Objects.equals(level,MemberLevelEnum._10.getCode())){
//            return new VipStrategy();
//        }else if(Objects.equals(level,MemberLevelEnum._20.getCode())){
//            return new DbzStrategy();
//        }
//        return new NomalStrategy();
        return new VipStrategy(level);
    }

    /**
     * 获取用户不同等级 积分比例
     * @param level
     * @return
     */
    public BigDecimal ratio(Integer level){
        return creat(level).ratio();
    }

}
