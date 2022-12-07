package com.pinet.rest.service.impl;

import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.UserConstant;
import com.pinet.core.enums.ErrorCodeEnum;
import com.pinet.core.enums.LoginEnum;
import com.pinet.core.exception.LoginException;
import com.pinet.core.result.Result;
import com.pinet.core.util.IPUtils;
import com.pinet.core.util.JWTUtils;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.dto.SmsDto;
import com.pinet.rest.entity.vo.LoginResponse;
import com.pinet.rest.entity.vo.WxLoginResult;
import com.pinet.rest.service.ICustomerService;
import com.pinet.rest.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service("wxLoginService")
public class WxLoginServiceImpl implements ILoginService {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RedisUtil redisUtil;

    @Value("${jwt.access_token.expire}")
    private Long expireTime;

    @Override
    public LoginResponse login(WxLoginResult wxLoginResult) {
        Customer customer = customerService.getByQsOpenId(wxLoginResult.getOpenid());

        if(customer != null){
            customer.setLastLoginIp(IPUtils.getIpAddr(request));
            customer.setLastLoginTime(System.currentTimeMillis());
            customer.setQsOpenId(wxLoginResult.getOpenid());
            customerService.updateById(customer);
        }else {
            customer = insertCustomer(wxLoginResult.getOpenid(),"",LoginEnum.WX);
        }

        String token = JWTUtils.generateToken(""+customer.getCustomerId());
        //token 有效时长
        redisUtil.set(UserConstant.USER_TOKEN+customer.getCustomerId(),token,expireTime, TimeUnit.SECONDS);

        LoginResponse response = new LoginResponse();
        response.setUserInfo(customer);
        response.setAccessToken(token);
        response.setExpireTime(LocalDateTime.now().plusSeconds(expireTime));
        return response;
    }

    @Override
    public LoginResponse login(SmsDto smsDto) {
        if(StringUtil.isEmpty(smsDto.getCode())){
            throw new LoginException(ErrorCodeEnum.SMS_EMPTY);
        }
        String code = redisUtil.get(CommonConstant.SMS_CODE_LOGIN + smsDto.getPhone());
        if(StringUtil.isEmpty(code)){
            throw new LoginException(ErrorCodeEnum.SMS_EXPIRED);
        }
        if(!code.equals(smsDto.getCode())){
            throw new LoginException(ErrorCodeEnum.SMS_ERROR);
        }

        Customer customer = customerService.getByPhone(smsDto.getPhone());
        if(customer != null){
            customer.setLastLoginIp(IPUtils.getIpAddr(request));
            customer.setLastLoginTime(System.currentTimeMillis());
            customerService.updateById(customer);
        }else {
            customer = insertCustomer(null,smsDto.getPhone(),LoginEnum.PHONE_CODE);
        }

        String token = JWTUtils.generateToken(""+customer.getCustomerId());
        //token 有效时长
        redisUtil.set(UserConstant.USER_TOKEN+customer.getCustomerId(),token,expireTime, TimeUnit.SECONDS);

        LoginResponse response = new LoginResponse();
        response.setUserInfo(customer);
        response.setAccessToken(token);
        response.setExpireTime(LocalDateTime.now().plusSeconds(expireTime));
        return response;
    }

    /**
     * 注册新用户
     * @param openId
     * @param loginEnum
     * @param phone
     * @return
     */
    private Customer insertCustomer(String openId,String phone ,LoginEnum loginEnum){
        //新增用户
        Customer customer = new Customer();
        String ip = IPUtils.getIpAddr(request);
        long now = System.currentTimeMillis();
        customer.setLastLoginIp(ip);
        customer.setLastLoginTime(now);
        customer.setCreateIp(ip);
        customer.setCreateTime(now);
        customer.setIsNew(0);
        if(loginEnum.equals(LoginEnum.WX)){
            customer.setQsOpenId(openId);
        }else {
            customer.setPhone(phone);
        }
        customerService.save(customer);
        return customer;
    }
}
