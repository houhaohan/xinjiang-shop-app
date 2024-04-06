package com.pinet.rest.aspect;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.pinet.core.util.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;



/**
 * @program: xinjiang-shop-app
 * @description: 全局日志打印
 * @author: hhh
 * @create: 2022-12-02 14:15
 **/
@Aspect
@Component
@Slf4j
@Order(-5)
public class WebLogAspect {

    static ThreadLocal<Long> localVar = new ThreadLocal<>();


    //定义切点
    @Pointcut("execution(public * com.pinet.rest.controller..*.*(..))")
    public void exec() {
    }

    @Before("exec()")
    public void doBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        localVar.set(System.currentTimeMillis());
        log.info("==> 请求者IP：" + IPUtils.getIpAddr() + "\n"
                + "==> 请求时间：" + DateUtil.now() + "\n"
                + "==> 请求接口：" + request.getMethod() + " " + request.getRequestURL() + "\n"
                + "==> 请求方法：" + joinPoint.getTarget().getClass().getName() + "." + method.getName() + "\n"
                + "==> 参数内容：" + Arrays.toString(joinPoint.getArgs()) + "\n");
    }

    @AfterReturning(pointcut = "exec()", returning = "object")
    public void afterReturning(Object object) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Long startTime = localVar.get();
        Long endTime = System.currentTimeMillis();
        long cost = endTime - startTime;
        log.info("==> 返回时间：" + DateUtil.now() + "\n"
                + "==> 请求接口：" + request.getMethod() + " " + request.getRequestURL() + "\n"
                + "==> 返回结果：" + JSON.toJSONString(object) + "\n"
                + "==> 耗时：" + cost + " ms\n"
                + "=============================================================================== end\n"
        );
        localVar.remove();
    }
}
