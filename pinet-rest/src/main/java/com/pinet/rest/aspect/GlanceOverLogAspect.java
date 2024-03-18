package com.pinet.rest.aspect;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.pinet.core.exception.PinetException;
import com.pinet.rest.service.IProductGlanceOverService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @description: xinjiang-shop-mini
 * @author: chengshuanghui
 * @date: 2024-03-14 16:28
 */
@Aspect
@Component
public class GlanceOverLogAspect {
    @Autowired
    private IProductGlanceOverService productGlanceOverService;

    @Pointcut("@annotation(com.pinet.rest.annotation.GlanceOverLog)")
    public void pointcut() {
    }

    @After("pointcut()")
    public void doAfter(JoinPoint point) {
        List<Object> args = Arrays.asList(point.getArgs());
        Long id = (Long)args.get(0);
        productGlanceOverService.updateGlanceOverTimes(id);
    }
}
