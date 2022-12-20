package com.pinet.rest.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.pinet.core.result.Result;
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
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/{version}/login")
@Api(tags = "登入")
@Slf4j
public class LoginController {

    @RequestMapping("/getAccessToken")
    public Result<WxToken> getAccessToken(){
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx87d8bb0f6665f9d5&secret=37a14809201fbe3640334c64d5bb554e&js_code=JSCODE&grant_type=authorization_code";
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
    public Result<UserInfo> wxLogin(@RequestBody WxLoginRequest request){
        try{
            ILoginService loginService = SpringContextUtils.getBean("wxLoginService", ILoginService.class);
            UserInfo userInfo = loginService.login(request);
            return Result.ok(userInfo);
        }
        catch (WxErrorException e){
            log.error("微信登入失败，失败原因=======》{}",e.getMessage());
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
    public Result<?> smsLogin(@RequestBody SmsLoginRequest request){
        try{
            ILoginService loginService = SpringContextUtils.getBean("phoneLoginService", ILoginService.class);
            UserInfo response = loginService.login(request);
            return Result.ok(response);
        }catch (Exception e){
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
            ILoginService loginService = SpringContextUtils.getBean("phoneLoginService", ILoginService.class);
            loginService.logout(token);
            return Result.ok();
        }catch (Exception e){
            log.error("手机验证码登入失败，失败原因=======》{}",e.getMessage());
        }
        return Result.error(500,"退出失败");
    }
}
