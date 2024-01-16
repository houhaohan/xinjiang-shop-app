package com.pinet.rest.entity.enums;

import lombok.Getter;

@Getter
public enum DiscountEnums {

    _100(1L,100,"不打折"),
    _95(2L,95,"9.5折"),
    _90(3L,90,"9折"),
    _85(4L,85,"8.5折"),
    _80(5L,80,"8折"),
    _75(6L,75,"7.5折"),
    _70(7L,70,"7折"),
    _65(8L,65,"6.5折"),
    _60(9L,60,"6折"),
    _55(10L,55,"5.5折"),
    _50(11L,50,"5折"),
    _30(12L,30,"3折"),
    _20(13L,20,"2折"),
    _10(14L,10,"1折"),
    _0(15L,0,"免费");


    private Long id;
    private Integer discount;
    private String description;

    DiscountEnums(Long id, Integer discount,String description) {
        this.id = id;
        this.discount = discount;
        this.description = description;
    }

    /**
     * 通过code取D
     * @param id 值
     * @return
     */
    public static DiscountEnums getById(Long id) {
        if (id == null) {
            return null;
        }
        for (DiscountEnums enums : DiscountEnums.values()) {
            if (enums.getId().equals(id)) {
                return enums;
            }
        }
        return null;
    }

    /**
     * 通过code取D
     * @param discount 值
     * @return
     */
    public static DiscountEnums getByDiscount(Integer discount) {
        if (discount == null) {
            return null;
        }
        for (DiscountEnums enums : DiscountEnums.values()) {
            if (enums.getDiscount().equals(discount)) {
                return enums;
            }
        }
        return null;
    }

    /**
     * 通过code取D
     * @param discount 值
     * @return
     */
    public static String getDescriptionByDiscount(Integer discount) {
        if (discount == null) {
            return null;
        }
        for (DiscountEnums enums : DiscountEnums.values()) {
            if (enums.getDiscount().equals(discount)) {
                return enums.getDescription().substring(0,enums.getDescription().length() -1);
            }
        }
        return null;
    }
}
