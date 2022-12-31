package com.pinet.rest.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.pinet.common.redis.util.RedisUtil;
import com.pinet.core.constants.UserConstant;
import com.pinet.core.exception.LoginException;
import com.pinet.core.result.Result;
import com.pinet.core.util.JwtTokenUtils;
import com.pinet.core.util.OkHttpUtil;
import com.pinet.core.util.SpringContextUtils;
import com.pinet.core.version.ApiVersion;
import com.pinet.rest.entity.request.SmsLoginRequest;
import com.pinet.rest.entity.request.WxLoginRequest;
import com.pinet.rest.entity.vo.UserInfo;
import com.pinet.rest.entity.vo.WxToken;
import com.pinet.rest.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/{version}/login")
@Api(tags = "登入")
@Slf4j
public class LoginController {

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/getAccessToken")
    public Result<WxToken> getAccessToken(){
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx89ad3dd778a3e6ff&secret=4e0c7a50e7c358eb290cc994ee03766d&js_code=021hIUll2COTua4wkHkl2KILrz3hIUlb&grant_type=authorization_code";
        String result = OkHttpUtil.get(url, null);
        WxToken wxToken = JSONObject.parseObject(result, WxToken.class);
        return Result.ok(wxToken);
    }

    /**
     * 参考：https://blog.csdn.net/weixin_45411101/article/details/124491929
     * 微信小程序登入
     * @param request
     * @return
     */
    @PostMapping("/wx")
    @ApiOperation("微信登入")
    @ApiVersion(1)
    public Result<UserInfo> wxLogin(@Validated @RequestBody WxLoginRequest request){
        try{
            System.out.println(request.getCode());
            ILoginService loginService = SpringContextUtils.getBean("wxLoginService", ILoginService.class);
            UserInfo userInfo = loginService.login(request);
            return Result.ok(userInfo);
        }
        catch (WxErrorException e){
            log.error("微信登入失败，失败原因=======》{}",e.getMessage());
            return Result.error(500,e.getMessage());
        }
        catch (Exception e){
            log.error("微信登入失败，失败原因=======》{}",e.getMessage());
        }
        return Result.error(500,"登入失败");
    }

    /**
     * 参考：https://blog.csdn.net/weixin_45411101/article/details/124491929
     * 微信小程序登入
     * @param request
     * @return
     */
    @PostMapping("/getPhoneNumber")
    @ApiOperation("获取手机号")
    @ApiVersion(1)
    public Result<UserInfo> getPhoneNumber(@RequestBody WxLoginRequest request){
        try{
            System.out.println(request.getCode());
            ILoginService loginService = SpringContextUtils.getBean("wxLoginService", ILoginService.class);
            UserInfo userInfo = loginService.login(request);
            return Result.ok(userInfo);
        }
        catch (WxErrorException e){
            log.error("微信登入失败，失败原因=======》{}",e.getMessage());
            return Result.error(500,e.getMessage());
        }
        catch (Exception e){
            log.error("微信登入失败，失败原因=======》{}",e.getMessage());
        }
        return Result.error(500,"登入失败");
    }


    /**
     * 手机验证码登入
     * @param request
     * @return
     */
    @PostMapping("/sms_code")
    @ApiOperation("手机验证码登入")
    @ApiVersion(1)
    public Result<?> smsLogin(@Validated @RequestBody SmsLoginRequest request){
        try{
            ILoginService loginService = SpringContextUtils.getBean("phoneLoginService", ILoginService.class);
            UserInfo response = loginService.login(request);
            return Result.ok(response);
        }catch (LoginException e){
            return Result.error(500,e.getMsg());
        }
        catch (Exception e){
            log.error("手机验证码登入失败，失败原因=======》{}",e.getMessage());
        }
        return Result.error(500,"登入失败");
    }


    /**
     * 退出登入
     * @return
     */
    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    @ApiOperation("退出登入")
    @ApiVersion(1)
    public Result<?> logout(@RequestParam String token){
        try{
            redisUtil.delete(UserConstant.PREFIX_USER_TOKEN+token);
            return Result.ok();
        }catch (Exception e){
            log.error("手机验证码登入失败，失败原因=======》{}",e.getMessage());
        }
        return Result.error(500,"退出失败");
    }
}
