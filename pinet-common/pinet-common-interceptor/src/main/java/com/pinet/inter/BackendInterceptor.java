package com.pinet.inter;

import com.alibaba.fastjson.JSON;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.UserConstant;
import com.pinet.core.exception.PinetException;
import com.pinet.core.http.HttpStatus;
import com.pinet.core.result.Result;
import com.pinet.core.util.JwtTokenUtils;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.inter.annotation.NotTokenSign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class  BackendInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(BackendInterceptor.class);
    private static final String MINI_ACCESS_TOKEN = "accessToken";
    private static final String APP_ACCESS_TOKEN = "Authorization";

    @Resource
    private RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //app token 存在情况下,让它走 app token 的拦截器
        String appToken = request.getHeader(APP_ACCESS_TOKEN);
        if(StringUtil.isNotEmpty(appToken)){
            return true;
        }
        // 小程序token存在情况下
        String accessToken = request.getHeader(MINI_ACCESS_TOKEN);

        if (handler != null && handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            NotTokenSign notTokenSign = handlerMethod.getMethodAnnotation(NotTokenSign.class);
            // 过滤登入校验
            if (null != notTokenSign) {
                return true;
            }

            if(StringUtil.isEmpty(accessToken)){
                return true;
//                return error(request,response);
            }

            String userId = redisUtil.get(UserConstant.PREFIX_USER_TOKEN + accessToken);
            if(StringUtil.isEmpty(userId)){
                throw new PinetException("token过期，请重新登入");
            }

            boolean validate = JwtTokenUtils.validateToken(accessToken, Long.valueOf(userId));
            if(validate){
                if(redisUtil.getExpire(UserConstant.PREFIX_USER_TOKEN + accessToken) < 30 * 60){
                    redisUtil.expire(UserConstant.PREFIX_USER_TOKEN + accessToken,JwtTokenUtils.EXPIRE_TIME/1000, TimeUnit.SECONDS);
                }
            }else {
                String newToken = JwtTokenUtils.generateToken(Long.valueOf(userId));
                redisUtil.set(UserConstant.PREFIX_USER_TOKEN + newToken,userId,JwtTokenUtils.EXPIRE_TIME/1000,TimeUnit.SECONDS);
                response.setHeader(MINI_ACCESS_TOKEN,newToken);
            }
            ThreadLocalUtil.setUserId(Long.valueOf(userId));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    private boolean error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf8");
        response.getWriter().write(JSON.toJSONString(Result.error(HttpStatus.SC_FORBIDDEN, "系统没有权限访问")));
        logger.warn("{}系统没有直接访问权限，请检查token是否过期", request.getRequestURI());
        return false;
    }

}
