package com.pinet.rest.controller;

import com.alipay.api.internal.util.file.IOUtils;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.pinet.core.controller.BaseController;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.service.IOrdersService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: xinjiang-shop-app
 * @description: 回调controller
 * @author: hhh
 * @create: 2023-01-03 10:50
 **/
@RestController
@RequestMapping("/notify")
@Slf4j
public class NotifyController extends BaseController {

    @Resource
    private WxPayService miniPayService;

    @Resource
    private WxPayService appPayService;

    @Resource
    private IOrdersService ordersService;



    @RequestMapping("/order/wxApp/pay")
    @ApiOperation("微信app支付回调")
    public String wxAppPayNotify(HttpServletRequest request) {
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult notifyResult = appPayService.parseOrderNotifyResult(xmlResult);
            if ("SUCCESS".equals(notifyResult.getResultCode())) {
                OrderPayNotifyParam param = new OrderPayNotifyParam(Long.valueOf(notifyResult.getOutTradeNo()),notifyResult.getTimeEnd(),notifyResult.getTransactionId(),"weixin_app");
            }

            return WxPayNotifyResponse.success("成功");
        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因{}", e);
            return WxPayNotifyResponse.success("code:" + 9999 + "微信回调结果异常,异常原因:" + e.getMessage());
        }

    }


    @RequestMapping("/order/wxMini/pay")
    @ApiOperation("微信小程序支付回调")
    public String wxMiniPayNotify(HttpServletRequest request){
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult notifyResult = miniPayService.parseOrderNotifyResult(xmlResult);
            if ("SUCCESS".equals(notifyResult.getResultCode())) {
                OrderPayNotifyParam param = new OrderPayNotifyParam(Long.valueOf(notifyResult.getOutTradeNo()),notifyResult.getTimeEnd(),notifyResult.getTransactionId(),"weixin_mini");
            }
            return WxPayNotifyResponse.success("成功");
        }catch (Exception e){
            log.error("微信回调结果异常,异常原因{}", e);
            return WxPayNotifyResponse.success("code:" + 9999 + "微信回调结果异常,异常原因:" + e.getMessage());
        }
    }

}
