package com.pinet.rest.util;

import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.UserConstant;
import com.pinet.core.util.JwtTokenUtils;
import com.pinet.core.util.ReqResUtils;
import com.pinet.rest.service.ICustomerTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginUser {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ICustomerTokenService customerTokenService;

    public Long currentUserId(){
        HttpServletRequest request = ReqResUtils.request();
        String token = request.getHeader("access_token");

        Long userId = JwtTokenUtils.getUserIdFromToken(token);
        if(userId == null){
            return null;
        }
        Boolean flag = redisUtil.hasKey(UserConstant.PREFIX_USER_TOKEN + userId);
        if(flag != null && flag){
            return userId;
        }
        return null;
    }


    public Long getAppUserId(){
        HttpServletRequest request = ReqResUtils.request();
        String token = request.getHeader("Authorization");

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
        return customerTokenService.validateAndReturnCustomerId(token,terminal);
    }
}
