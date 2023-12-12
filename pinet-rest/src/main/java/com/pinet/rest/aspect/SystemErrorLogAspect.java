package com.pinet.rest.aspect;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.pinet.core.exception.PinetException;
import com.pinet.rest.service.ISystemErrorService;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @program: xinjiang-shop-app
 * @description: 系统异常记录
 * @author: hhh
 * @create: 2023-12-12 14:55
 **/
@Aspect
@Component
public class SystemErrorLogAspect {
    @Resource
    private ISystemErrorService systemErrorService;

    @Resource
    private HttpServletRequest httpServletRequest;

    /**
     * 设置切入点：这里直接拦截被@SystemErrorLog注解的方法
     */
    @Pointcut("@annotation(com.pinet.rest.annotation.SystemErrorLog)")
    public void pointcut() {
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void saveSysErrorLog(Throwable e) {
        //如果是自定义异常不记录
        if (e instanceof PinetException) {
            return;
        }
        //请求地址
        String requestAddress = httpServletRequest.getRequestURI();
        //异常堆栈信息转string
        String errorMsg = ExceptionUtil.getMessage(e);
        systemErrorService.add(requestAddress, errorMsg);
    }
}
