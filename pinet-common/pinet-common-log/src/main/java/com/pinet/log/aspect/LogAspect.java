package com.pinet.log.aspect;

import com.alibaba.fastjson.JSON;
import com.pinet.log.dto.OperationLog;
import com.pinet.log.annotation.Log;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

import static com.pinet.core.util.ReqResUtils.getServerIp;
import static com.pinet.core.util.ReqResUtils.getServerName;

@Aspect
@Component
public class LogAspect {

    private static Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 处理完业务请求后执行
     */
    public void doAfterReturning() {

    }

    /**
     * 抛出异常后执行
     */
    public void doAfterThrowing() {

    }


    protected void handleLog(final JoinPoint point,final Exception e,Object rs) {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Log log = method.getAnnotation(Log.class);
        if(log==null) return;

        OperationLog operationLog = new OperationLog();
        operationLog.setDesc(log.desc());
        operationLog.setEventType(log.type().name());

        if(e==null){
            operationLog.setSucc(true);
        }else{
            operationLog.setSucc(false);
            operationLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 500));
        }
        String className=point.getTarget().getClass().getName();
        String methodName = methodSignature.getName();

        // 获取当前用户
        //operationLog.setCurrentUserId();
        //operationLog.setCurrentUserIp();
        //operationLog.setCurrentUserName();
        // 获得当前服务及方法名,响应内容
        operationLog.setMethod(methodName);
        operationLog.setService(className);
        operationLog.setResponseContent(JSON.toJSONString(rs));

        // 获得请求参数
        operationLog.setServerIp(getServerIp());
        operationLog.setServerName(getServerName());


    }

}
