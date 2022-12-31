package com.pinet.inter;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pinet.core.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        this.strictInsertFill(metaObject, "createTime", () -> new Date(), Date.class);
        this.strictInsertFill(metaObject, "createBy", () -> userId == 0 ? null : userId, Long.class);

    }


    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        Long userId = ThreadLocalUtil.getUserLogin().getUserId();
        this.strictUpdateFill(metaObject, "updateTime", () -> new Date(), Date.class);
        this.strictUpdateFill(metaObject, "updateBy", () -> userId == 0 ? null : userId, Long.class);

    }
}
