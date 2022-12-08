package com.pinet.rest.util;

import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.UserConstant;
import com.pinet.core.util.JWTUtils;
import com.pinet.core.util.ReqResUtils;
import com.pinet.core.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class LoginUser {
    @Autowired
    private RedisUtil redisUtil;

    public String currentUserId(){
        HttpServletRequest request = ReqResUtils.request();
        String token = request.getHeader("access_token");

        String userId = JWTUtils.getUserId(token);
        if(StringUtil.isEmpty(userId)){
            return null;
        }
        Boolean flag = redisUtil.hasKey(UserConstant.PREFIX_USER_TOKEN + userId);
        if(flag != null && flag){
            return userId;
        }
        return null;
    }
}
