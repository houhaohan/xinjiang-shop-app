package com.pinet.rest.controller;

import com.alibaba.fastjson.JSONObject;
import com.pinet.component.oss.util.OssUtil;
import com.pinet.core.result.Result;
import com.pinet.core.util.OkHttpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wx")
public class LoginController {

    private static final String url = "https://api.weixin.qq.com/sns/jscode2session";
    @RequestMapping("/getAccessToken")
    public Result getAccessToken(){
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx87d8bb0f6665f9d5&secret=37a14809201fbe3640334c64d5bb554e&js_code=JSCODE&grant_type=authorization_code";
        String result = OkHttpUtil.get(url, null);

        return Result.ok(JSONObject.parseObject(result));
    }

    @RequestMapping("/login")
    public Result login(String code){
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx87d8bb0f6665f9d5&secret=37a14809201fbe3640334c64d5bb554e&js_code=JSCODE&grant_type=authorization_code";
        String result = OkHttpUtil.get(url, null);
        return Result.ok();
    }

    public static void main(String[] args) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx87d8bb0f6665f9d5&secret=37a14809201fbe3640334c64d5bb554e";
        String s = OkHttpUtil.get(url, null);
        System.out.println(s);

        OssUtil.getImageUrl("");
    }
}
