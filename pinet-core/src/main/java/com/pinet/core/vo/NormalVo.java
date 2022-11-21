package com.pinet.core.vo;

import lombok.Data;

@Data
public class NormalVo {
    private Integer code;
    private String text;


    public NormalVo() {

    }
    public NormalVo(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
