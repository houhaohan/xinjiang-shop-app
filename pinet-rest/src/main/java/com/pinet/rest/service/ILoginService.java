package com.pinet.rest.service;

import com.pinet.rest.entity.request.LoginRequest;
import com.pinet.rest.entity.vo.UserInfo;
import me.chanjar.weixin.common.error.WxErrorException;

public interface ILoginService {


    /**
     * 登入
     * @param request
     * @return
     */
    public UserInfo login(LoginRequest request) throws WxErrorException;


}
