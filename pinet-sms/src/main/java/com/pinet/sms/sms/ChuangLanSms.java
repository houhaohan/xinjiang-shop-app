package com.pinet.sms.sms;

import com.alibaba.fastjson.JSONObject;
import com.pinet.core.result.Result;
import com.pinet.sms.SmsInterface;
import com.pinet.sms.enums.SmsTemplate;
import com.pinet.sms.util.ChuangLanSmsUtil;
import java.util.HashMap;
import java.util.Map;

public class ChuangLanSms implements SmsInterface {

    //创蓝验证码API账号
    private static String ACCOUNT_CODE = "YZM3700413";
    private static String PASSWORD_CODE = "7RF9g0n4yk45a7";
    private static String SEND_URL_CODE = "http://smssh1.253.com/msg/v1/send/json";

    //通知短信
    public static String  ACCOUNT_MSG ="N7468275";
    public static String  PASSWORD_MSG ="8yGSghzre";
    public static String  SEND_URL_MSG ="http://smssh1.253.com/msg/v1/send/json";
    @Override
    public Result send(String phones, SmsTemplate smsTemplate, Map<String, String> data) {
        String code = data.get("code");
        Map map = new HashMap(6);
        //API账号
        map.put("account", ACCOUNT_CODE);
        //API密码
        map.put("password", PASSWORD_CODE);
        //短信内容
        map.put("msg","【未来巴扎】您的验证码是"+code+",10分钟内有效");
        //手机号
        map.put("phone",phones);
        //是否需要状态报告
        map.put("report","true");
        //自定义扩展码
        map.put("extend","123");
        JSONObject js = (JSONObject) JSONObject.toJSON(map);
        String response = ChuangLanSmsUtil.sendSmsByPost(SEND_URL_CODE, js.toString());
        JSONObject jsonObject = JSONObject.parseObject(response);
        String resCode = jsonObject.getString("code");
        return jsonObject != null && resCode.equals("0") ? Result.ok() : Result.error(jsonObject.getString("errorMsg"));
    }

    @Override
    public Result sendPickupOrderNoticeForShop(String phone, SmsTemplate smsTemplate, Map<String, String> data) {
        //店铺名称
        String shop_name = data.get("shop_name");
        //提货时间
        String pickup_time = data.get("pickup_time");
        Map map = new HashMap(6);
        map.put("account", ACCOUNT_MSG);
        map.put("password", PASSWORD_MSG);
//        map.put("msg","【未来巴扎】"+shop_name+"您有一笔自提订单，用户将于"+pickup_time+"上门自取，请注意及时配货。");
        map.put("msg","【未来巴扎】"+shop_name+"您有一笔自提订单，用户将于"+pickup_time+"上门自取，请注意及时配货，点 http://web.ypxlbz.com/orderForSms?id="+data.get("order_id")+"查看详情。");
        map.put("phone",phone);
        map.put("report","true");
        map.put("extend","123");
        map.put("uid",data.get("id"));
        JSONObject js = (JSONObject) JSONObject.toJSON(map);
        String response = ChuangLanSmsUtil.sendSmsByPost(SEND_URL_MSG, js.toString());
        JSONObject jsonObject = JSONObject.parseObject(response);
        String time = jsonObject.getString("time");
        String resCode = jsonObject.getString("code");
        return jsonObject != null && resCode.equals("0") ? Result.ok(time) :new Result(-100,jsonObject.getString("errorMsg"),time);
    }

    @Override
    public Result sendPickupOrderNoticeForUser(String phone, SmsTemplate smsTemplate, Map<String, String> data) {
        //用户昵称
        String nick_name = data.get("nick_name");
        String order_id = data.get("order_id");
        Map map = new HashMap(6);
        map.put("account", ACCOUNT_MSG);
        map.put("password", PASSWORD_MSG);
//        map.put("msg","【未来巴扎】尊敬的用户"+nick_name+"，您的订单已配货完成，请您在预定时间内前往店铺取货，如有问题请联系客服处理。");
        map.put("msg","【未来巴扎】尊敬的用户，您的订单已配货完成，请您在预定时间内前往店铺取货，如有问题请联系客服处理。点 http://web.ypxlbz.com/orderForSms?id="+order_id+" 查看清单。");
        map.put("phone",phone);
        map.put("report","true");
        map.put("extend","123");
        map.put("uid",data.get("id"));
        JSONObject js = (JSONObject) JSONObject.toJSON(map);
        String response = ChuangLanSmsUtil.sendSmsByPost(SEND_URL_MSG, js.toString());
        JSONObject jsonObject = JSONObject.parseObject(response);
        String resCode = jsonObject.getString("code");
        String time = jsonObject.getString("time");
        return jsonObject != null && resCode.equals("0") ? Result.ok(time) : new Result(-100,jsonObject.getString("errorMsg"),time);
    }
}