package com.pinet.rest.entity.enums;

import lombok.Getter;

/**
 * @description:vip等级枚举
 * @author: chengshuanghui
 * @date: 2024-06-04 12:17
 */
@Getter
public enum VipLevelEnum {
    VIP1(1,"VIP1", 0),
    VIP2(2,"VIP2", 500),
    VIP3(3,"VIP3",1000),
    VIP4(4,"VIP4",2000),
    VIP5(5,"VIP5",4000);

    private Integer level;
    private String name;
    private Integer minAmount;

    VipLevelEnum(Integer level, String name,Integer minAmount) {
        this.level = level;
        this.name = name;
        this.minAmount = minAmount;
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
}
