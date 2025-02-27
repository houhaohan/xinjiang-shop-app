package com.pinet.rest.websocket;

import com.alibaba.fastjson.JSONObject;
import com.pinet.core.result.Result;
import com.pinet.inter.annotation.NotTokenSign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("webSocketApi")
public class TestController {

	@Autowired
	private WebSocket webSocket;
 
    @PostMapping("/sendAll")
	@NotTokenSign
    public Result<String> sendAll(@RequestBody JSONObject jsonObject) {
    	Result<String> result = new Result<String>();
    	String message = jsonObject.getString("message");
    	JSONObject obj = new JSONObject();
    	obj.put("cmd", "topic");
		obj.put("msgId", "M0001");
		obj.put("msgTxt", message);
    	webSocket.sendAllMessage("你好啊，我是群发小组手");
        result.ok("群发！");
        return result;
    }
    
    @PostMapping("/sendUser")
	@NotTokenSign
    public Result<String> sendUser(@RequestBody JSONObject jsonObject) {
    	Result<String> result = new Result<String>();
    	String userId = jsonObject.getString("userId");
    	String message = jsonObject.getString("message");
    	JSONObject obj = new JSONObject();
    	obj.put("cmd", "user");
    	obj.put("userId", userId);
		obj.put("msgId", "M0001");
		obj.put("msgTxt", message);
        webSocket.sendOneMessage(userId, obj.toJSONString());
        result.ok("单发");
        return result;
    }
    
}