package com.pinet.rest.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.dynamic.datasource.annotation.DS;
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

@Service("phoneLoginService")
@RequiredArgsConstructor
public class PhoneLoginServiceImpl implements ILoginService {

    private final ICustomerService customerService;

    private final RedisUtil redisUtil;

    private final WxMaService wxMaService;

    @Resource
    private ICustomerCouponService customerCouponService;

    @Resource
    private ICustomerBalanceService customerBalanceService;

    @Override
    public UserInfo login(LoginRequest loginRequest) throws WxErrorException {
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
            if(StringUtil.isBlank(customer.getQsOpenId())){
                WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(smsLoginRequest.getWxCode());
                String openid = sessionInfo.getOpenid();
                customer.setQsOpenId(openid);
            }
            customerService.updateById(customer);
        }else {
            WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(smsLoginRequest.getWxCode());
            String openid = sessionInfo.getOpenid();
            String ip = IPUtils.getIpAddr();
            customer = Customer.builder()
                    .createTime(System.currentTimeMillis())
                    .createIp(ip)
                    .lastLoginIp(ip)
                    .lastLoginTime(System.currentTimeMillis())
                    .phone(smsLoginRequest.getPhone())
                    .qsOpenId(openid)
                    .active(1)
                    .uuid(String.valueOf((int)((Math.random()*9+1)*Math.pow(10,7))))
                    .build();
            customerService.save(customer);
            //发放新人优惠券
            customerCouponService.grantNewCustomerCoupon(customer.getCustomerId());
            //添加用户账户表
            customerBalanceService.addByCustomerId(customer.getCustomerId());
        }

        String userId = "" + customer.getCustomerId();
        String token = JwtTokenUtils.generateToken(customer.getCustomerId());
        redisUtil.set(UserConstant.PREFIX_USER_TOKEN+token,userId,JwtTokenUtils.EXPIRE_TIME/1000, TimeUnit.SECONDS);

        //todo 登入日志
        UserInfo userInfo = new UserInfo();
        userInfo.setAccess_token(token);
        userInfo.setExpireTime(LocalDateTime.now().plusSeconds(JwtTokenUtils.EXPIRE_TIME/1000));
        userInfo.setUser(customer);
        return userInfo;
    }
}
