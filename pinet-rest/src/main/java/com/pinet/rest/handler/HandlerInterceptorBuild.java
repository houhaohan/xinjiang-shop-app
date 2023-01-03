package com.pinet.rest.handler;

import com.alibaba.fastjson.JSON;
import com.pinet.core.entity.Token;
import com.pinet.core.exception.PinetException;
import com.pinet.core.http.HttpResult;
import com.pinet.core.util.AppJwtTokenUtil;
import com.pinet.core.util.StringUtil;
import com.pinet.core.util.ThreadLocalUtil;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.service.ICustomerTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * APP token的拦截器
 */
@Component
public class HandlerInterceptorBuild implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(HandlerInterceptorBuild.class);

    private static final String APP_ACCESS_TOKEN = "Authorization";
    private static final String MINI_ACCESS_TOKEN = "accessToken";
    private static final String TOKEN_HEAD_PREFIX = "Bearer ";


    @Autowired
    private ICustomerTokenService customerTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(StringUtil.isNotEmpty(request.getHeader(MINI_ACCESS_TOKEN))){
            return true;
        }

        String appToken = request.getHeader(APP_ACCESS_TOKEN);
        logger.info("appToken为:{}",appToken);
        if(StringUtil.isEmpty(appToken)){
            if (handler != null && handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                NotTokenSign notTokenSign = handlerMethod.getMethodAnnotation(NotTokenSign.class);
                // 过滤登入校验
                if (null != notTokenSign) {
                    return true;
                }
                return error(request,response);
            }
        }

        //获取终端
        String userAgent = request.getHeader("User-Agent");
        int terminal = 0;
        if(userAgent != null){
            if(userAgent.contains("okhttp")){
                terminal = 2;
            }else if(userAgent.contains("iPhone")){
                terminal = 1;
            }
        }

        if(StringUtil.isEmpty(appToken)){
            appToken = "";
        }else {
            if (appToken.startsWith(TOKEN_HEAD_PREFIX)){
                appToken = appToken.substring(TOKEN_HEAD_PREFIX.length());
            }
        }
        Long userId = customerTokenService.validateAndReturnCustomerId(appToken, terminal);
        //如果token已过期，但是未加入黑名单也在保留范围内，则进行刷新token操作，在请求头内直接返回新的token
        if(userId != null && AppJwtTokenUtil.isTokenExpired(appToken)){
            Token customerToken = AppJwtTokenUtil.generateTokenObject(String.valueOf(userId) , request);
            customerTokenService.refreshToken(customerToken, appToken);
            response.setHeader(APP_ACCESS_TOKEN, TOKEN_HEAD_PREFIX + customerToken.getToken());
        }else {
//            throw new PinetException("token过期，请重新登入");
            logger.error("登录认证失败,token{}",appToken);
        }
        ThreadLocalUtil.setUserId(userId == null ? 0 : Long.valueOf(userId));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }

    private boolean error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf8");
        response.getWriter().write(JSON.toJSONString(HttpResult.error(403, "系统没有权限访问")));
        logger.warn("{}系统没有直接访问权限，请检查token是否过期", request.getRequestURI());
        return false;
    }
}
