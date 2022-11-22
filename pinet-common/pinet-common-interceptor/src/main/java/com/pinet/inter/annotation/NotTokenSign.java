package com.pinet.inter.annotation;

import java.lang.annotation.*;

/**
 * @Auther: chengshuanghui
 * @Date: 2022/11/20
 * @Description:
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotTokenSign {
}
