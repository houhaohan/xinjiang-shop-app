package com.pinet.rest.service;

import com.pinet.core.entity.Token;
import com.pinet.rest.entity.CustomerToken;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户登录token 服务类
 * </p>
 *
 * @author wlbz
 * @since 2022-12-12
 */
public interface ICustomerTokenService extends IService<CustomerToken> {
    /**
     * 验证token是否有效，如果有效则回传customerId，否则回传null
     *
     * @param token
     * @param terminal 终端，可判断多个终端是否同时在线
     * */
    Long validateAndReturnCustomerId(String token, int terminal);


    boolean refreshToken(Token token, String oldToken);


}
