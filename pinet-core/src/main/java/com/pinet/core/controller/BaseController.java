package com.pinet.core.controller;


import com.pinet.core.util.ThreadLocalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 获取当前用户ID
     * @return
     */
    protected Long currentUserId(){
        ThreadLocalUtil.UserLogin userLogin = currentUser();
        if(userLogin == null){
            return 0L;
        }
        return userLogin.getUserId();
    }

    /**
     * 获取当前用户
     * @return
     */
    protected ThreadLocalUtil.UserLogin currentUser(){
        return ThreadLocalUtil.getUserLogin();
    }
}
