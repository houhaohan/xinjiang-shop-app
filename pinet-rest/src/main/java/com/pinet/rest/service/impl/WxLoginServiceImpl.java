package com.pinet.rest.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaUserService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.alibaba.fastjson.JSONObject;
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
import com.pinet.rest.service.ICustomerBalanceService;
import com.pinet.rest.service.ICustomerCouponService;
import com.pinet.rest.service.ICustomerService;
import com.pinet.rest.service.ILoginService;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service("wxLoginService")
@RequiredArgsConstructor
public class WxLoginServiceImpl implements ILoginService {

    private final ICustomerService customerService;

    private final WxMaService wxMaService;

    private final RedisUtil redisUtil;

    @Resource
    private ICustomerCouponService customerCouponService;

    @Resource
    private ICustomerBalanceService customerBalanceService;

    @Override
    public UserInfo login(LoginRequest loginRequest) throws WxErrorException {
        WxLoginRequest wxLoginRequest = (WxLoginRequest)loginRequest;
        WxMaUserService userService = wxMaService.getUserService();
        WxMaJscode2SessionResult sessionInfo = userService.getSessionInfo(wxLoginRequest.getCode());
        WxMaPhoneNumberInfo phoneNoInfo = userService.getPhoneNoInfo(sessionInfo.getSessionKey(), wxLoginRequest.getEncryptedData(), wxLoginRequest.getIv());
        if(phoneNoInfo == null || StringUtil.isBlank(phoneNoInfo.getPhoneNumber())){
            throw new LoginException("获取手机号失败");
        }

        Customer customer = customerService.getByPhone(phoneNoInfo.getPhoneNumber());
        if(customer != null){
            if(customer.getActive() == 0){
                throw new LoginException("该用户已禁用");
            }
            customer.setLastLoginIp(IPUtils.getIpAddr());
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

            String ip = IPUtils.getIpAddr();
            customer = Customer.builder()
                    .createTime(System.currentTimeMillis())
                    .createIp(ip)
                    .lastLoginIp(ip)
                    .lastLoginTime(System.currentTimeMillis())
                    .qsOpenId(sessionInfo.getOpenid())
//                    .nickname(wxLoginRequest.getNickName())
                    .nickname("微信用户")
                    .avatar("http://image.ypxlbz.com/qingshi/image/8d342da4670d490fb81df2723b97293e.png")
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
}
