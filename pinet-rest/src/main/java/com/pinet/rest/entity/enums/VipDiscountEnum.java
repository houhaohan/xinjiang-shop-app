package com.pinet.rest.entity.enums;

import lombok.Getter;

/**
 * @description: VIP 折扣
 * @author: chengshuanghui
 * @date: 2024-06-05 10:23
 */
@Getter
public enum VipDiscountEnum {
    VIP1(1,"VIP1",100,""),
    VIP2(2,"VIP2",98,"VIP 9.8折优惠"),
    VIP3(3,"VIP3",97,"VIP 9.7折优惠"),
    VIP4(4,"VIP4",96,"VIP 9.6折优惠"),
    VIP5(5,"VIP5",95,"VIP 9.5折优惠");

    private Integer level;
    private String name;
    private Integer discount;
    private String description;

    VipDiscountEnum(Integer level, String name, Integer discount,String description) {
        this.level = level;
        this.name = name;
        this.discount = discount;
        this.description = description;
    }

    public static VipDiscountEnum getEnumByCode(Integer level) {
        if (level == null) {
            return null;
        }
        for (VipDiscountEnum e : VipDiscountEnum.values()) {
            if (e.getLevel().equals(level)) {
                return e;
            }
        }
        return null;
    }

    public static Double getDiscountValue(VipDiscountEnum e) {
        return e.discount * 0.01;
    }

    public static void main(String[] args) {
        Double discountDouble = getDiscountValue(VipDiscountEnum.VIP3);
        System.out.println(discountDouble);
    }

}
