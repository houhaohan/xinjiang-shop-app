package com.pinet.rest.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.UserConstant;
import com.pinet.core.exception.LoginException;
import com.pinet.core.util.IPUtils;
import com.pinet.core.util.JwtTokenUtils;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.request.LoginRequest;
import com.pinet.rest.entity.request.WxLoginRequest;
import com.pinet.rest.entity.vo.UserInfo;
import com.pinet.rest.service.*;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service("wxLoginService")
@RequiredArgsConstructor
public class WxLoginServiceImpl implements ILoginService {

    private final ICustomerService customerService;

    private final WxMaService wxMaService;

    private final RedisUtil redisUtil;

    private final ICustomerCouponService customerCouponService;

    private final ICustomerBalanceService customerBalanceService;

    /**
     * 此接口只用于新用户登入，老用户登入走 oldUserLogin 接口
     * @param loginRequest
     * @return
     */
    @Override
    @DSTransactional
    public UserInfo login(LoginRequest loginRequest)  throws WxErrorException {
        WxLoginRequest wxLoginRequest = (WxLoginRequest)loginRequest;
        if(StringUtil.isBlank(wxLoginRequest.getOpenid())){
            throw new LoginException("openId为空");
        }

        WxMaUserService userService = wxMaService.getUserService();
        String sessionKey = wxLoginRequest.getSessionKey();
        if(StringUtil.isNotBlank(wxLoginRequest.getCode())){
            WxMaJscode2SessionResult sessionInfo = userService.getSessionInfo(wxLoginRequest.getCode());
            sessionKey = sessionInfo.getSessionKey();
        }

        WxMaPhoneNumberInfo phoneNoInfo = userService.getPhoneNoInfo(sessionKey, wxLoginRequest.getEncryptedData(), wxLoginRequest.getIv());
        if(phoneNoInfo == null || StringUtil.isBlank(phoneNoInfo.getPhoneNumber())){
            throw new LoginException("获取手机号失败");
        }

        Customer customer = customerService.getByPhone(phoneNoInfo.getPhoneNumber());
        if (customer != null){
            if(customer.getActive() == 0){
                throw new LoginException("该用户已禁用");
            }
            customer.setLastLoginIp(IPUtils.getIpAddr());
            customer.setLastLoginTime(System.currentTimeMillis());
            customer.setQsOpenId(wxLoginRequest.getOpenid());
            customerService.updateById(customer);
        }else {
            //创建新用户
            String ip = IPUtils.getIpAddr();
             customer = Customer.builder()
                    .createTime(System.currentTimeMillis())
                    .createIp(ip)
                    .lastLoginIp(ip)
                    .lastLoginTime(System.currentTimeMillis())
                    .qsOpenId(wxLoginRequest.getOpenid())
                    .nickname(UserConstant.DEFAULT_USER_NAME)
                    .avatar(UserConstant.DEFAULT_USER_AVATAR)
                    .sex(wxLoginRequest.getGender())
                    .phone(phoneNoInfo.getPhoneNumber())
                    .active(1)
                    .uuid(String.valueOf((int)((Math.random()*9+1)*Math.pow(10,7))))
                    .build();
            customerService.save(customer);
            //发放新人优惠券
            customerCouponService.grantNewCustomerCoupon(customer.getCustomerId());
            //添加用户账户表
            customerBalanceService.addByCustomerId(customer.getCustomerId());
        }

        String token = JwtTokenUtils.generateToken(customer.getCustomerId());
        redisUtil.set(UserConstant.PREFIX_USER_TOKEN+token,String.valueOf(customer.getCustomerId()),JwtTokenUtils.EXPIRE_TIME/1000, TimeUnit.SECONDS);
        UserInfo userInfo = new UserInfo();
        userInfo.setAccess_token(token);
        userInfo.setExpireTime(LocalDateTime.now().plusSeconds(JwtTokenUtils.EXPIRE_TIME/1000));
        userInfo.setUser(customer);
        return userInfo;
    }

    @Override
    public UserInfo oldUserLogin(String code) throws WxErrorException {
        UserInfo userInfo = new UserInfo();
        WxMaUserService userService = wxMaService.getUserService();
        WxMaJscode2SessionResult sessionInfo = userService.getSessionInfo(code);
        userInfo.setSessionInfo(sessionInfo);
        Customer customer = customerService.getByQsOpenId(sessionInfo.getOpenid());
        if(customer == null){
            //新用户再调一遍login接口
            return userInfo;
        }

        if(customer.getActive() == 0){
            throw new LoginException("该用户已禁用");
        }
        customer.setLastLoginIp(IPUtils.getIpAddr());
        customer.setLastLoginTime(System.currentTimeMillis());
        customer.setQsOpenId(sessionInfo.getOpenid());
        customerService.updateById(customer);

        userInfo.setUser(customer);

        String newToken = JwtTokenUtils.generateToken(customer.getCustomerId());
        redisUtil.set(UserConstant.PREFIX_USER_TOKEN+newToken,String.valueOf(customer.getCustomerId()),JwtTokenUtils.EXPIRE_TIME/1000, TimeUnit.SECONDS);
        userInfo.setAccess_token(newToken);
        userInfo.setExpireTime(LocalDateTime.now().plusSeconds(JwtTokenUtils.EXPIRE_TIME/1000));
        return userInfo;
    }
}
