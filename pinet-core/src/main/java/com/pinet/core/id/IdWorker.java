package com.pinet.core.id;

import java.util.UUID;

/**
 * 高效GUID产生算法(sequence),基于Snowflake实现64位自增ID算法。 <br>
 * 优化开源项目 http://git.oschina.net/yu120/sequence
 */
public class IdWorker {

    /**
     * 主机和进程的机器码
     */
    private static final SnowFlake worker = new SnowFlake();

    public static long getId() {
        return worker.nextId();
    }

    public static void main(String[] args) {
        System.out.println(IdWorker.getId());
    }

    public static String getIdStr() {
        return String.valueOf(worker.nextId());
    }

    public static synchronized String get32UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
