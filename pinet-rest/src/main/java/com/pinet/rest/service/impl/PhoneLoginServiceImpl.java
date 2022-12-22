package com.pinet.rest.service.impl;

import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.UserConstant;
import com.pinet.core.enums.ErrorCodeEnum;
import com.pinet.core.exception.LoginException;
import com.pinet.core.util.IPUtils;
import com.pinet.core.util.JwtTokenUtils;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.request.LoginRequest;
import com.pinet.rest.entity.request.SmsLoginRequest;
import com.pinet.rest.entity.vo.UserInfo;
import com.pinet.rest.service.ICustomerService;
import com.pinet.rest.service.ILoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service("phoneLoginService")
@RequiredArgsConstructor
public class PhoneLoginServiceImpl implements ILoginService {

    private final ICustomerService customerService;

    private final RedisUtil redisUtil;

    @Override
    public UserInfo login(LoginRequest loginRequest) {
        SmsLoginRequest smsLoginRequest = (SmsLoginRequest)loginRequest;
        String code = redisUtil.get(CommonConstant.SMS_CODE_LOGIN + smsLoginRequest.getPhone());
        if(StringUtil.isEmpty(code)){
            throw new LoginException(ErrorCodeEnum.SMS_EXPIRED);
        }
        if(!code.equals(smsLoginRequest.getCode())){
            throw new LoginException(ErrorCodeEnum.SMS_ERROR);
        }

        Customer customer = customerService.getByPhone(smsLoginRequest.getPhone());
        if(customer != null){
            if(customer.getActive() == 0){
                throw new LoginException(ErrorCodeEnum.CUSTOMER_NOT_ACTIVE);
            }
            customer.setLastLoginIp(IPUtils.getIpAddr());
            customer.setLastLoginTime(System.currentTimeMillis());
            customerService.updateById(customer);
        }else {
            String ip = IPUtils.getIpAddr();
            customer = Customer.builder()
                    .createTime(System.currentTimeMillis())
                    .createIp(ip)
                    .lastLoginIp(ip)
                    .lastLoginTime(System.currentTimeMillis())
                    .phone(smsLoginRequest.getPhone())
                    .active(1)
                    .uuid(String.valueOf((int)((Math.random()*9+1)*Math.pow(10,7))))
                    .build();
            customerService.save(customer);
        }

        String userId = "" + customer.getCustomerId();
        String token = JwtTokenUtils.generateToken(customer.getCustomerId());
        cacheToken(userId,token);

        //todo 登入日志
        UserInfo userInfo = new UserInfo();
        userInfo.setAccess_token(token);
        userInfo.setExpireTime(LocalDateTime.now().plusSeconds(JwtTokenUtils.EXPIRE_TIME/1000));
        userInfo.setUser(customer);
        return userInfo;
    }

    @Override
    public void logout(String token) {
        Long userId = JwtTokenUtils.getUserIdFromToken(token);
        if(userId == null){
            return;
        }
        redisUtil.delete(UserConstant.PREFIX_USER_TOKEN+userId);
    }

    /**
     * 缓存token
     * @param userId
     * @param token
     */
    private void cacheToken(String userId,String token){
        redisUtil.set(UserConstant.PREFIX_USER_TOKEN+userId,token,JwtTokenUtils.EXPIRE_TIME/1000, TimeUnit.SECONDS);
    }
}
