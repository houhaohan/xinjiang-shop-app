package com.pinet.core.util.id;

/**
 * @author chengshuanghui
 * @date 2022/04/21
 * ID工具类
 */
public class IdUtil {

    private static IdWorker idWorker = new IdWorker(1);

    private IdUtil() {

    }

    public static long generateId() {
        return idWorker.nextId();
    }
}