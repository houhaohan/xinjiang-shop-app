package com.pinet.rest.controller.login;

import com.alibaba.fastjson.JSONObject;
import com.pinet.core.result.Result;
import com.pinet.core.util.OkHttpUtil;
import com.pinet.rest.entity.dto.SmsDto;
import com.pinet.rest.entity.vo.LoginResponse;
import com.pinet.rest.entity.vo.WxLoginResult;
import com.pinet.rest.entity.vo.WxToken;
import com.pinet.rest.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/login")
@Api(tags = "登入")
@Slf4j
public class LoginController {
    @Value("wx.app_id")
    private String app_id;
    @Value("wx.app_secret")
    private String app_secret;
    @Value("wx.authorization_code")
    private String authorization_code;

    @Autowired
    private ILoginService loginService;


    @RequestMapping("/getAccessToken")
    public Result<WxToken> getAccessToken(){
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx87d8bb0f6665f9d5&secret=37a14809201fbe3640334c64d5bb554e&js_code=JSCODE&grant_type=authorization_code";
        String result = OkHttpUtil.get(url, null);
        WxToken wxToken = JSONObject.parseObject(result, WxToken.class);
        return Result.ok(wxToken);
    }

    /**
     * 微信小程序登入
     * @param code
     * @return
     */
    @PostMapping("/wx")
    @ApiOperation("微信登入")
    public Result<LoginResponse> wxLogin(@RequestParam String code){
        try{
            String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+app_id+"&secret="+app_secret+"&js_code="+code+"&grant_type="+authorization_code;
            String result = OkHttpUtil.get(url, null);
            WxLoginResult wxLoginResult = JSONObject.parseObject(result, WxLoginResult.class);
            LoginResponse response = loginService.login(wxLoginResult);
            return Result.ok(response);
        }catch (Exception e){
            log.error("微信登入失败，失败原因=======》{}",e.getMessage());
        }
        return Result.error(500,"登入失败");
    }


    /**
     * 手机验证码登入
     * @param smsDto
     * @return
     */
    @PostMapping("/sms_code")
    @ApiOperation("手机验证码登入")
    public Result<?> smsLogin(@RequestBody SmsDto smsDto){
        try{
            LoginResponse response = loginService.login(smsDto);
            return Result.ok(response);
        }catch (Exception e){
            log.error("手机验证码登入失败，失败原因=======》{}",e.getMessage());
        }
        return Result.error(500,"登入失败");
    }
}
