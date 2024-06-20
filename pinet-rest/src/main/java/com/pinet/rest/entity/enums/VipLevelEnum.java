package com.pinet.rest.entity.enums;

import com.pinet.core.util.BigDecimalUtil;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @description:vip等级枚举
 * @author: chengshuanghui
 * @date: 2024-06-04 12:17
 */
@Getter
public enum VipLevelEnum {
    VIP1(1,"VIP1", BigDecimal.ZERO,"vip1_gift_bag"),
    VIP2(2,"VIP2", new BigDecimal("500"),"vip2_gift_bag"),
    VIP3(3,"VIP3",new BigDecimal("1000"),"vip3_gift_bag"),
    VIP4(4,"VIP4",new BigDecimal("2000"),"vip4_gift_bag"),
    VIP5(5,"VIP5",new BigDecimal("4000"),"vip5_gift_bag");

    private Integer level;
    private String name;
    private BigDecimal minAmount;
    private String giftBagKey;

    VipLevelEnum(Integer level, String name,BigDecimal minAmount,String giftBagKey) {
        this.level = level;
        this.name = name;
        this.minAmount = minAmount;
        this.giftBagKey = giftBagKey;
    }

    public static VipLevelEnum next(Integer level){
        if(level < VipLevelEnum.VIP5.getLevel()){
            level = level + 1;
        }
        return VipLevelEnum.getEnumByCode(level);
    }

    /**
     * 通过code取枚举
     *
     * @param level 值
     * @return
     */
    public static VipLevelEnum getEnumByCode(Integer level) {
        if (level == null) {
            return null;
        }
        for (VipLevelEnum enums : VipLevelEnum.values()) {
            if (enums.getLevel().equals(level)) {
                return enums;
            }
        }
        return null;
    }

    /**
     * 通过code取 VIP名称
     *
     * @param level 值
     * @return
     */
    public static String getNameByCode(Integer level) {
        if (level == null) {
            return null;
        }
        for (VipLevelEnum e : VipLevelEnum.values()) {
            if (e.getLevel().equals(level)) {
                return e.getName();
            }
        }
        return null;
    }

    /**
     * 通过金额取等级
     *
     * @param amount 值
     * @return
     */
    public static VipLevelEnum getEnumByAmount(BigDecimal amount) {
        if (amount == null) {
            return VipLevelEnum.VIP1;
        }
        if(BigDecimalUtil.le(amount,VipLevelEnum.VIP2.getMinAmount())){
            return VipLevelEnum.VIP1;
        }
        if(BigDecimalUtil.le(amount,VipLevelEnum.VIP3.getMinAmount())){
            return VipLevelEnum.VIP2;
        }
        if(BigDecimalUtil.le(amount,VipLevelEnum.VIP4.getMinAmount())){
            return VipLevelEnum.VIP3;
        }
        if(BigDecimalUtil.le(amount,VipLevelEnum.VIP5.getMinAmount())){
            return VipLevelEnum.VIP4;
        }
        return VipLevelEnum.VIP5;
    }

    public static void main(String[] args) {
        VipLevelEnum e = getEnumByAmount(new BigDecimal("582.7"));
        System.out.println(e);
    }
}
