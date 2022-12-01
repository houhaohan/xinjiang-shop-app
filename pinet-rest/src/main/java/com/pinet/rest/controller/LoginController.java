package com.pinet.rest.controller;

import com.alibaba.fastjson.JSONObject;
import com.pinet.core.result.Result;
import com.pinet.core.util.OkHttpUtil;
import com.pinet.rest.entity.vo.WxLoginResult;
import com.pinet.rest.entity.vo.WxToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx")
public class LoginController {

    private static final String url = "https://api.weixin.qq.com/sns/jscode2session";


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
    @PostMapping("/login")
    public Result login(@RequestParam String code){
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx87d8bb0f6665f9d5&secret=37a14809201fbe3640334c64d5bb554e&js_code=JSCODE&grant_type=authorization_code";
        String result = OkHttpUtil.get(url, null);
        WxLoginResult wxLoginResult = JSONObject.parseObject(result, WxLoginResult.class);
        return Result.ok(wxLoginResult);
    }
}
