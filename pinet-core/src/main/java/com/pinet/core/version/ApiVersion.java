package com.pinet.core.version;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * Created by acer hzpinet
 * 2018/3/21.
 *
 * @author linfake
 * @version 1.0.0
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface ApiVersion {

    /**
     * 标识版本号
     * @return
     */
    int value();
}
