package com.pinet.mq.enums;

/**
 * 消息类型
 */
public enum MsgTypeEnum {

    /**
     * 一般消息
     */
    NORMAL("normal"),
    /**
     * 延迟消息
     */
    DELAYED("delayed"),
    /**
     * 顺序消息
     */
    SEQUENCE("sequnce");

    private String type;
    MsgTypeEnum(String type){
        this.type = type;
    };
}
