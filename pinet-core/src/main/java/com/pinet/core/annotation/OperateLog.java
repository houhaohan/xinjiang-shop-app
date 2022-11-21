package com.pinet.core.annotation;

import com.pinet.core.enums.OperateTypeEnum;

import java.lang.annotation.*;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {
    /**
     * 日志内容
     *
     * @return
     */
    String value() default "";

    /**
     * 日志类型
     *
     * @return 0:操作日志;1:登录日志;2:定时任务;
     */
    int logType() default 0;

    /**
     * 操作日志类型
     *
     * @return （1查询，2保存，3删除）
     */
    OperateTypeEnum operateType() ;
}
