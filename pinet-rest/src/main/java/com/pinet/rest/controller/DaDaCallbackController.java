package com.pinet.rest.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imdada.open.platform.callback.internal.CallbackParam;
import com.pinet.core.result.Result;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.service.IDaDaService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dada")
@Api(tags = "达达配送")
@Slf4j
public class DaDaCallbackController {
    @Autowired
    private IDaDaService daDaService;

    /**
     * 达达配送回调 http://www.testxingjianghouse.ypxlbz.com/house/qingshi/api/dada/callback
     * @param callbackParam
     */
    @RequestMapping(value = "/callback")
    @NotTokenSign
    public JSONObject callback(@RequestBody JSONObject param){
        log.info("达达配送回调param=======>{}", param.toJSONString());
        //{"messageType":1,"messageBody":"","createTime":1694157741}
        try{
            daDaService.callback(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject result = new JSONObject();
        result.put("status","ok");
        return result;
    }


    @RequestMapping(value = "/orderStatus/callback")
    @NotTokenSign
    public JSONObject orderStatusCallback(@RequestBody CallbackParam param){
        try{
           //{"order_status":1,"cancel_reason":"","update_time":1694158211,"cancel_from":0,"signature":"d0c956211692ea68d11daf0d81bfdec0","dm_id":0,"is_finish_code":false,"order_id":"2131312311","client_id":"1526014050948743168"}
            daDaService.syncOrderStatus(null);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject result = new JSONObject();
        result.put("status","ok");
        return result;
    }
}
