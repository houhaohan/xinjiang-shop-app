package com.pinet.core.enums;

public enum OperateTypeEnum {

    QUERY(1),
    SAVE(2),
    DELETE(3);

    private Integer code;

    OperateTypeEnum(Integer code){
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }
}
