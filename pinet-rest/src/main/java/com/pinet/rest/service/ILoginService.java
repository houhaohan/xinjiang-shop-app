package com.pinet.rest.service;

import com.pinet.rest.entity.dto.SmsDto;
import com.pinet.rest.entity.vo.LoginResponse;
import com.pinet.rest.entity.vo.WxLoginResult;

public interface ILoginService {


    /**
     * 微信登入
     * @param wxLoginResult
     * @return
     */
    public LoginResponse login(WxLoginResult wxLoginResult);

    /**
     * 短信验证码登入
     * @param smsDto
     * @return
     */
    public LoginResponse login(SmsDto smsDto);
}
