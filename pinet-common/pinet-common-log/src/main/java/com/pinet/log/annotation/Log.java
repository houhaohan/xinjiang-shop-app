package com.pinet.log.annotation;

import com.pinet.log.enums.BusinessType;

import java.lang.annotation.*;

@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 描述信息
     *
     * @return
     */
    public String desc() default "";

    /**
     * 功能类型
     * @return
     */
    public BusinessType type() default BusinessType.OTHER;



}
