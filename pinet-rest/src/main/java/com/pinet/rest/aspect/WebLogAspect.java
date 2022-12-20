package com.pinet.rest.aspect;

import com.alibaba.fastjson.JSONObject;
import com.pinet.core.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    //定义切点
    @Pointcut("execution(public * com.pinet.rest.controller..*.*(..))")
    public void exec() {
    }

    @Around("exec()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String[]> stringMap = md5ImgParam(new HashMap(request.getParameterMap()));
        Object[] args = point.getArgs();
        Object retVal = point.proceed(args);

        log.info("\n\t请求IP: {}\n\t请求路径: {}\n\t请求方式: {}\n\t请求参数: {}\n\t返回值: {}",
                request.getRemoteAddr(), request.getRequestURL(), request.getMethod()
                , JSONObject.toJSONString(stringMap), JSONObject.toJSONString(retVal));

        return retVal;
    }

    /**
     *    处理base64图片
     */
    private Map<String, String[]> md5ImgParam(Map<String, String[]> parameterMap) {
        String[] imgpaths = parameterMap.get("imgPath");

        String[] list = new String[0];
        if (null != imgpaths && imgpaths.length != 0) {
            List<String> imgpathFinal = new ArrayList<>();
            //获取第一个字符串
            String imgPathStr = imgpaths[0];
            if (imgPathStr.contains("分")) {
                String[] split = imgPathStr.split("分");
                for (String s : split) {
                    String s1 = MD5Util.md5(s);
                    imgpathFinal.add(s1);
                }
                list = imgpathFinal.toArray(new String[split.length]);
            } else if (imgPathStr.contains("|")) {
                String[] split = imgPathStr.split("\\|");
                for (String s : split) {
                    String s1 = MD5Util.md5(s);
                    imgpathFinal.add(s1);
                }
                list = imgpathFinal.toArray(new String[split.length]);
            } else {
                list = new String[1];
                list[0] = MD5Util.md5(imgPathStr);
            }
        }
        if (list.length > 0) {
            parameterMap.put("imgPath", list);
        }
        return parameterMap;
    }
}
