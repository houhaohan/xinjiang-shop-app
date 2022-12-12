package com.pinet.inter;

import com.alibaba.fastjson.JSON;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.UserConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.http.HttpResult;
import com.pinet.core.util.JwtTokenUtils;
import com.pinet.core.util.StringUtil;
import com.pinet.inter.annotation.NotTokenSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class BackendInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(BackendInterceptor.class);
    private static final String ACCESS_TOKEN = "access_token";

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

            String userId = redisUtil.get(UserConstant.PREFIX_USER_TOKEN + accessToken);
            if(StringUtil.isEmpty(userId)){
                throw new PinetException("token过期，请重新登入");
            }

            boolean validate = JwtTokenUtils.validateToken(accessToken, Long.valueOf(userId));
            if(validate){
                if(redisUtil.getExpire(UserConstant.PREFIX_USER_TOKEN + accessToken) < 10 * 60){
                    redisUtil.expire(UserConstant.PREFIX_USER_TOKEN + accessToken,JwtTokenUtils.EXPIRE_TIME/1000, TimeUnit.SECONDS);
                }
            }else {
                String newToken = JwtTokenUtils.generateToken(Long.valueOf(userId));
                redisUtil.set(UserConstant.PREFIX_USER_TOKEN + newToken,userId,JwtTokenUtils.EXPIRE_TIME/1000,TimeUnit.SECONDS);
                response.setHeader(ACCESS_TOKEN,newToken);
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
