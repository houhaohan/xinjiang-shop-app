package com.pinet.rest.service.common;

import com.pinet.core.entity.BaseEntity;
import com.pinet.core.util.ThreadLocalUtil;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * @program: xinjiang-shop-app
 * @description:
 * @author: hhh
 * @create: 2022-12-29 13:56
 **/
@Service
public class CommonService {

    /**
     * 给属性（createTime,createBy,updateTime,updateBy,delFlag）设置默认值
     */
    public void setDefInsert(BaseEntity baseEntity){
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        if (customerId == null){
            customerId = 0L;
        }
        Date now = new Date();
        baseEntity.setCreateBy(customerId);
        baseEntity.setCreateTime(now);
        baseEntity.setUpdateBy(customerId);
        baseEntity.setUpdateTime(now);
        baseEntity.setDelFlag(0);
    }


    /**
     * 给对象设置updateTime,updateBy默认值
     */
    public void setDefUpdate(BaseEntity baseEntity){
        Long customerId = ThreadLocalUtil.getUserLogin().getUserId();
        if (customerId == null){
            customerId = 0L;
        }
        Date now = new Date();
        baseEntity.setUpdateBy(customerId);
        baseEntity.setUpdateTime(now);
    }
}
