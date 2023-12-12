package com.pinet.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: xinjiang-shop-app
 * @description: 系统异常记录注解
 * @author: hhh
 * @create: 2023-12-12 14:54
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemErrorLog {
}
