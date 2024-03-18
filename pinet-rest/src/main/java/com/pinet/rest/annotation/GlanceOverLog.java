package com.pinet.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: xinjiang-shop-mini
 * @description: 商品浏览日志 注解
 * @author: chengshuanghui
 * @create: 2024-03-14 14:54
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface GlanceOverLog {

}
