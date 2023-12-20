package com.pinet.rest.entity.enums;

import lombok.Getter;

@Getter
public enum DeliveryPlatformEnum {
    ZPS("ZPS","自配送"),
    DADA("DADA","达达");

    private String code;

    private String name;

    DeliveryPlatformEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
