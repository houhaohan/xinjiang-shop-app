package com.pinet.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pinet.core.exception.PinetException;
import com.pinet.core.ApiErrorEnum;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SignUtil {
    private static Logger logger = LoggerFactory.getLogger(SignUtil.class);

    // md5、rsa
    private String signType;

    public SignUtil() {
    }

    public SignUtil(String signType) {
        this.signType = signType;
    }

    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type") || key.equalsIgnoreCase("t")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    public static String createLinkString(Map<String, String> params, String appSecret) {
        params = paraFilter(params);
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        prestr = prestr + "&app_secret=" + appSecret;
        return prestr;
    }

    /**
     * 生成签名
     *
     * @param params    请求参数封装为map对象
     * @param appSecret appsecret
     * @return
     */
    public static String sign(Map<String, String> params, String appSecret) {
        String sign = MD5Util.md5(createLinkString(params, appSecret));
        return sign;
    }

    /**
     * 验证签名
     * @param reqBody  包含sign的json字符串
     * @param appSecret 从数据库中读取appSecret
     */
    public static void verifySign(String reqBody, String appSecret) {
        if (StringUtils.isEmpty(reqBody)) {
            throw new PinetException(ApiErrorEnum.PARAM_T_ERROR.getMsg(), ApiErrorEnum.PARAM_T_ERROR.getCode());
        }
        if (StringUtil.isEmpty(appSecret)) {
            throw new PinetException(ApiErrorEnum.PARAM_KEY_ERROR.getMsg(), ApiErrorEnum.PARAM_T_ERROR.getCode());

        }
        JSONObject jsonObject = JSON.parseObject(reqBody);
        String sign = jsonObject.getString("sign");
        if (StringUtil.isEmpty(sign)) {
            throw new PinetException(ApiErrorEnum.PARAM_SIGN_ERROR.getMsg(), ApiErrorEnum.PARAM_SIGN_ERROR.getCode());
        }
        Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : entrySet) {
            map.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        String prestr = createLinkString(map, appSecret);
        String verifiedSign = MD5Util.md5(prestr).toLowerCase();
        logger.info("请求参数:{},签名结果:{}", reqBody, verifiedSign);
        if (!verifiedSign.equals(sign.toLowerCase())) {
            throw new PinetException(ApiErrorEnum.PARAM_AUTH_ERROR.getMsg(), ApiErrorEnum.PARAM_AUTH_ERROR.getCode());
        }

    }

    public static void main(String[] args) {
        Map<String,String> map = new HashMap<>();
        map.put("appId","0");
        map.put("date",DateUtil.convertDate2String(new Date(),"yyyyMMdd"));
//        map.put("method","post");


        String appSecret = "79d2ccdb2cb640cd9725a2e6cd93bf26";
        String sign = sign(map, appSecret);
        System.out.println(sign);
        //18B7F6E0FAE08773066C2A558ADF6D8D


        String reqBody = "{\"sign\":\"3FE5DB51B464532BD2C23206C835C4BF\",\"appId\":\"9521952286\",\"date\":\"20220512\"}";
        //verifySign(reqBody,appSecret);

        Map<String,Object> signMap = new HashMap<>();
        signMap.put("sign","3FE5DB51B464532BD2C23206C835C4BF");
        signMap.put("appId",0L);
        signMap.put("date", "20220512");

        //appId=9521952286&date=20220512&app_secret=79d2ccdb2cb640cd9725a2e6cd93bf26
        String prestr = "appId=0&date=20220512&app_secret=79d2ccdb2cb640cd9725a2e6cd93bf26";
        String verifiedSign = MD5Util.md5(prestr).toLowerCase();
        System.out.println(verifiedSign);
        //0cdb2ea00eab52faf285df0075c37255
//        verifySign(JSONObject.toJSONString(signMap),appSecret);
    }
}
