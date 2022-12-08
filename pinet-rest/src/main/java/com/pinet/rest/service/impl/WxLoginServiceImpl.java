package com.pinet.rest.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.CommonConstant;
import com.pinet.core.constants.UserConstant;
import com.pinet.core.enums.ErrorCodeEnum;
import com.pinet.core.exception.LoginException;
import com.pinet.core.util.IPUtils;
import com.pinet.core.util.JWTUtils;
import com.pinet.core.util.StringUtil;
import com.pinet.rest.entity.Customer;
import com.pinet.rest.entity.request.LoginRequest;
import com.pinet.rest.entity.request.SmsLoginRequest;
import com.pinet.rest.entity.request.WxLoginRequest;
import com.pinet.rest.entity.vo.UserInfo;
import com.pinet.rest.service.ICustomerService;
import com.pinet.rest.service.ILoginService;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service("wxLoginService")
@RequiredArgsConstructor
public class WxLoginServiceImpl implements ILoginService {

    private final ICustomerService customerService;

    private final HttpServletRequest request;

    private final WxMaService wxMaService;

    private final RedisUtil redisUtil;

    @Override
    public UserInfo login(LoginRequest loginRequest) throws WxErrorException {
        WxLoginRequest wxLoginRequest = (WxLoginRequest)loginRequest;
        WxMaUserService userService = wxMaService.getUserService();
        WxMaJscode2SessionResult sessionInfo = userService.getSessionInfo(wxLoginRequest.getCode());
        Customer customer = customerService.getByQsOpenId(sessionInfo.getOpenid());
        if(customer != null){
            if(customer.getActive() == 0){
                throw new LoginException("该用户已禁用");
            }
            customer.setLastLoginIp(IPUtils.getIpAddr(request));
            customer.setLastLoginTime(System.currentTimeMillis());
            customer.setQsOpenId(sessionInfo.getOpenid());
            customerService.updateById(customer);
        }else {
            //创建新用户
            if(sessionInfo == null){
                throw new LoginException("获取session失败");
            }
            if(sessionInfo.getOpenid() == null){
                throw new LoginException("获取openid失败");
            }

            //获取用户信息
            WxMaUserInfo userInfo = userService.getUserInfo(sessionInfo.getSessionKey(), wxLoginRequest.getEncryptedData(), wxLoginRequest.getIv());

            //获取手机号
            WxMaPhoneNumberInfo wxMaPhoneNumberInfo = userService.getPhoneNoInfo(sessionInfo.getSessionKey(), wxLoginRequest.getEncryptedData(), wxLoginRequest.getIv());
            if(wxMaPhoneNumberInfo == null || StringUtil.isEmpty(wxMaPhoneNumberInfo.getPhoneNumber())){
                throw new LoginException("获取手机号失败");
            }
            String ip = IPUtils.getIpAddr(request);
            customer = Customer.builder()
                    .createTime(System.currentTimeMillis())
                    .createIp(ip)
                    .lastLoginIp(ip)
                    .lastLoginTime(System.currentTimeMillis())
                    .qsOpenId(sessionInfo.getOpenid())
                    .nickname(userInfo.getNickName())
                    .avatar(userInfo.getAvatarUrl())
                    .sex(Integer.valueOf(userInfo.getGender()))
                    .phone(wxMaPhoneNumberInfo.getPhoneNumber())
                    .active(1)
                    .build();
            customerService.save(customer);
        }

        String userId = "" + customer.getCustomerId();
        String token = JWTUtils.generateToken(userId);
        cacheToken(userId,token);

        UserInfo userInfo = new UserInfo();
        userInfo.setAccess_token(token);
        userInfo.setExpireTime(LocalDateTime.now().plusSeconds(JWTUtils.expire));
        userInfo.setUser(customer);
        return userInfo;
    }

    /**
     * 缓存token
     * @param userId
     * @param token
     */
    private void cacheToken(String userId,String token){
        redisUtil.set(UserConstant.PREFIX_USER_TOKEN+userId,token,JWTUtils.expire, TimeUnit.SECONDS);
        redisUtil.set(UserConstant.PREFIX_REFRESH_TOKEN+token, userId,JWTUtils.expire+1800, TimeUnit.SECONDS);
    }
}
