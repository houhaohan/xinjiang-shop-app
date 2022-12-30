package com.pinet.inter;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.pinet.core.util.ThreadLocalUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if (metaObject.hasGetter("createTime")){
            Long userId = ThreadLocalUtil.getUserLogin().getUserId();
            this.setFieldValByName("createTime", new Date(),metaObject);
            this.setFieldValByName("createBy", userId == 0 ? null : userId,metaObject);
        }
    }


    @Override
    public void updateFill(MetaObject metaObject) {
        if (metaObject.hasGetter("updateTime")){
            Long userId = ThreadLocalUtil.getUserLogin().getUserId();
            this.setFieldValByName("updateTime", new Date(),metaObject);
            this.setFieldValByName("updateBy", userId == 0 ? null : userId,metaObject);
        }
    }
}
