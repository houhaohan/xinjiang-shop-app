package com.pinet.inter;

import com.alibaba.fastjson.JSON;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.UserConstant;
import com.pinet.core.http.HttpResult;
import com.pinet.core.util.StringUtil;
import com.pinet.inter.annotation.NotTokenSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BackendInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(BackendInterceptor.class);
    private static final String ACCESS_TOKEN = "access_token";
    @Value("${spring.application.name}")
    private String applicationName;

    @Resource
    private RedisUtil redisUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = request.getHeader(ACCESS_TOKEN);

        if (handler != null && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            NotTokenSign notTokenSign = handlerMethod.getMethodAnnotation(NotTokenSign.class);
            // 过滤登入校验
            if (null != notTokenSign) {
                return true;
            }

            if(StringUtil.isEmpty(accessToken)){
                return error(request,response);
            }
            String tokenData = redisUtil.get(UserConstant.PREFIX_USER_TOKEN + accessToken);
            if(StringUtil.isEmpty(tokenData)){
                return error(request,response);
            }
        }
        return true;
    }



    private boolean error(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf8");
        response.getWriter().write(JSON.toJSONString(HttpResult.error(403, "系统没有权限访问")));
        logger.warn("{}系统没有直接访问权限，请检查token是否过期", request.getRequestURI());
        return false;
    }

}
