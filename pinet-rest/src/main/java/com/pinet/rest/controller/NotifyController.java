package com.pinet.rest.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.internal.util.file.IOUtils;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.pinet.core.controller.BaseController;
import com.pinet.inter.annotation.NotTokenSign;
import com.pinet.rest.config.properties.AliAppProperties;
import com.pinet.rest.entity.param.OrderPayNotifyParam;
import com.pinet.rest.entity.param.OrderRefundNotifyParam;
import com.pinet.rest.service.IOrdersService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

    @Resource
    private AliAppProperties aliAppProperties;



    @RequestMapping("/order/wxApp/pay")
    @ApiOperation("微信app支付回调")
    @NotTokenSign
    public String wxAppPayNotify(HttpServletRequest request) {
        log.info("进入微信app支付回调");
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult notifyResult = appPayService.parseOrderNotifyResult(xmlResult);
            if ("SUCCESS".equals(notifyResult.getResultCode())) {
                OrderPayNotifyParam param = new OrderPayNotifyParam(Long.valueOf(notifyResult.getOutTradeNo()),DateUtil.parse(notifyResult.getTimeEnd(),"yyyyMMddHHmmss"),notifyResult.getTransactionId(),"weixin_app");
                ordersService.orderPayNotify(param);
            }

            return WxPayNotifyResponse.success("成功");
        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因{}", e);
            return WxPayNotifyResponse.fail("code:" + 9999 + "微信回调结果异常,异常原因:" + e.getMessage());
        }

    }


    @RequestMapping("/order/wxMini/pay")
    @ApiOperation("微信小程序支付回调")
    @NotTokenSign
    public String wxMiniPayNotify(HttpServletRequest request){
        log.info("进入微信小程序支付回调");
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayOrderNotifyResult notifyResult = miniPayService.parseOrderNotifyResult(xmlResult);
            if ("SUCCESS".equals(notifyResult.getResultCode())) {
                OrderPayNotifyParam param = new OrderPayNotifyParam(Long.valueOf(notifyResult.getOutTradeNo()),DateUtil.parse(notifyResult.getTimeEnd(),"yyyyMMddHHmmss") ,notifyResult.getTransactionId(),"weixin_mini");
                ordersService.orderPayNotify(param);
            }
            return WxPayNotifyResponse.success("成功");
        }catch (Exception e){
            log.error("微信回调结果异常,异常原因{}", e);
            return WxPayNotifyResponse.fail("code:" + 9999 + "微信回调结果异常,异常原因:" + e.getMessage());
        }
    }


    @RequestMapping("/order/aliApp/pay")
    @ApiOperation("支付宝支付回调")
    @NotTokenSign
    public String aliAppPayNotify(@RequestParam Map<String, String> params){
        log.info("支付宝支付通知,正在执行,通知参数:{}", JSON.toJSONString(params));
        String result = "failure";
        try {
            //异步通知验签
            boolean signVerified = AlipaySignature.rsaCheckV1(params,aliAppProperties.getPublicKeyAlipay(),
                    AlipayConstants.CHARSET_UTF8,
                    AlipayConstants.SIGN_TYPE_RSA2);
            if (!signVerified) {
                log.error("支付成功,异步通知验签失败!");
                return result;
            }
            log.info("支付成功,异步通知验签成功!");
            //1.验证out_trade_no 是否为商家系统中创建的订单号
            String outTradeNo = params.get("out_trade_no");
            //2.判断 total_amount 是否确实为该订单的实际金额
            String totalAmount = params.get("total_amount");

            //支付时间
            String gmtPayment = params.get("gmt_payment");
            //支付宝交易号
            String tradeNo = params.get("trade_no");

            //4.验证 app_id 是否为该商家本身
            String appId = params.get("app_id");
            if (!appId.equals(aliAppProperties.getAppid())){
                log.error("app_id校验失败");
                return result;
            }
            //在支付宝的业务通知中，只有交易通知状态为 TRADE_SUCCESS 或 TRADE_FINISHED 时，支付宝才会认定为买家付款成功
            String tradeStatus = params.get("trade_status");
            if (!"TRADE_SUCCESS".equals(tradeStatus) && !"TRADE_FINISHED".equals(tradeStatus)){
                log.error("支付未成功");
                return result;
            }

            OrderPayNotifyParam param = new OrderPayNotifyParam(Long.valueOf(outTradeNo), DateUtil.parse(gmtPayment,"yyyy-MM-dd HH:mm:ss"),tradeNo,"alipay_app");
            ordersService.orderPayNotify(param);
            result = "success";
        }catch (Exception e){
            log.error("支付宝回调结果异常,异常原因{}", e);
        }

        return result;
    }



    @RequestMapping("/order/wxApp/refund")
    @ApiOperation("微信app退款回调")
    @NotTokenSign
    public String wxOrderRefund(HttpServletRequest request){
        log.info("进入微信App退款回调");
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayRefundNotifyResult wxPayRefundNotifyResult = appPayService.parseRefundNotifyResult(xmlResult);

            //退款订单号
            String refundNo = wxPayRefundNotifyResult.getReqInfo().getOutRefundNo();

            //微信退款单号
            String refundId = wxPayRefundNotifyResult.getReqInfo().getRefundId();

            //退款成功
            if("SUCCESS".equals(wxPayRefundNotifyResult.getReqInfo().getRefundStatus())) {
                //自己处理订单退款成功后的业务逻辑，需要判断该退款订单是否已经退款过，否则可能会重复调用
                ordersService.orderRefundNotify(new OrderRefundNotifyParam(Long.valueOf(refundNo),refundId));
            }
            //必须要给微信返回回调成功状态，否则微信会一直回调
            return WxPayNotifyResponse.success("成功");
        } catch (Exception e) {
            log.error("微信退款回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail("code:"+9999+"微信回调结果异常,异常原因:"+e.getMessage());
        }
    }



    @RequestMapping("/order/wxMini/refund")
    @ApiOperation("微信小程序退款回调")
    @NotTokenSign
    public String wxMiniOrderRefund(HttpServletRequest request){
        log.info("进入微信小程序退款回调");
        try {
            String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            WxPayRefundNotifyResult wxPayRefundNotifyResult = miniPayService.parseRefundNotifyResult(xmlResult);
            //退款订单号
            String refundNo = wxPayRefundNotifyResult.getReqInfo().getOutRefundNo();

            //微信退款单号
            String refundId = wxPayRefundNotifyResult.getReqInfo().getRefundId();


            //退款成功
            if("SUCCESS".equals(wxPayRefundNotifyResult.getReqInfo().getRefundStatus())) {
                //自己处理订单退款成功后的业务逻辑，需要判断该退款订单是否已经退款过，否则可能会重复调用
                ordersService.orderRefundNotify(new OrderRefundNotifyParam(Long.valueOf(refundNo),refundId));
            }
            //必须要给微信返回回调成功状态，否则微信会一直回调
            return WxPayNotifyResponse.success("成功");
        } catch (Exception e) {
            log.error("微信退款回调结果异常,异常原因{}", e.getMessage());
            return WxPayNotifyResponse.fail("code:"+9999+"微信回调结果异常,异常原因:"+e.getMessage());
        }
    }



}
