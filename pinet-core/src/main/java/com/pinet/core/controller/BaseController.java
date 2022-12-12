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
        return ThreadLocalUtil.getUserLogin().getUserId();
    }


}
