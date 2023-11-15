package com.pinet.rest.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imdada.open.platform.callback.internal.CallbackParam;
import com.imdada.open.platform.client.internal.req.order.AddOrderReq;
import com.imdada.open.platform.client.internal.resp.order.AddOrderResp;
import com.imdada.open.platform.exception.RpcException;
import com.pinet.core.result.Result;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.service.IDaDaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
     * @param param
     */
    @RequestMapping(value = "/callback")
    @NotTokenSign
    public JSONObject callback(@RequestBody CallbackParam param){
        log.info("达达配送回调param=======>{}", JSON.toJSONString(param));
        try{
            daDaService.callback(param);
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
            daDaService.syncOrderStatus(param);
        }catch (Exception e){
            e.printStackTrace();
        }
        JSONObject result = new JSONObject();
        result.put("status","ok");
        return result;
    }


    /**
     * 达达运费查询
     * eg: {"shopNo":"f1b801be8af3483a","originId":"1000","cargoPrice":"31","isPrepay":0,"receiverName":"小灰灰","receiverAddress":"上海市浦东新区东方渔人码头","receiverPhone":"15868805739","callback":"https://www.baidu.com","cargoWeight":"0.5","receiverLat":"30.18020900","receiverLng":"120.21314900"}
     * @param req
     * @return
     */
    @ApiOperation("达达运费查询")
    @RequestMapping(value = "/queryDeliverFee")
    public Result<AddOrderResp> queryDeliverFee(@RequestBody AddOrderReq req){
        try{
            AddOrderResp resp = daDaService.queryDeliverFee(req);
            return Result.ok(resp);
        }catch (RpcException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.error("查询运费失败");
    }


}
