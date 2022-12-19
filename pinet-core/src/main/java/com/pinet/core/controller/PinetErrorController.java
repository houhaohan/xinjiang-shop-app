package com.pinet.core.controller;


import com.alibaba.fastjson.JSONObject;
import com.pinet.core.http.HttpResult;
import com.pinet.core.http.HttpStatus;
import com.pinet.core.ApiErrorEnum;
import com.pinet.core.exception.PinetException;
import com.pinet.core.util.MD5Util;
import com.pinet.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RestControllerAdvice
public class PinetErrorController implements ErrorController {
    @RequestMapping("/error")
    public HttpResult error(HttpServletRequest request, HttpServletResponse response) {
        Object obj = request.getAttribute("javax.servlet.error.exception");
        Object forwardurl = request.getAttribute("javax.servlet.error.request_uri");
        Object errorCode = request.getAttribute("javax.servlet.error.status_code");

        String msg = "";
        if (forwardurl != null) {
            log.error("请求地址{}发生错误", forwardurl);
        }
        if (obj != null && obj instanceof PinetException) {
            PinetException uguessException = (PinetException) obj;
            msg = uguessException.getMsg();
            return HttpResult.error(uguessException.getCode(), msg);
        }
        if (obj != null && obj instanceof Exception) {
            Exception exception = (Exception) obj;
            msg = exception.getMessage() == null ? ApiErrorEnum.ERROR.getMsg() : StringUtil.subBeforeColon(exception.getMessage());
        }
        if (errorCode != null && errorCode instanceof Integer) {
            int code = Integer.parseInt(errorCode.toString());
            if (code == HttpStatus.SC_NOT_FOUND) {
                msg = ApiErrorEnum.ERROR_NOT_FOUNT.getMsg();
            }
        }
        return HttpResult.error(response.getStatus(), StringUtil.isEmpty(msg) ? "系统发生未知的错误" : msg);
    }


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(PinetException.class)
    public HttpResult handler(PinetException e,HttpServletRequest request) {
        Map<String, String[]> stringMap = md5ImgParam(new HashMap<>(request.getParameterMap()));
        log.error("\n\t请求IP: {}\n\t请求路径: {}\n\t请求方式: {}\n\t请求参数: {}\n\t自定义异常！:{}",
                request.getRemoteAddr(), request.getRequestURL(), request.getMethod()
                , JSONObject.toJSONString(stringMap), e);
        log.error(e.getMessage(), e);
        return HttpResult.error(ApiErrorEnum.ERROR.getCode(), e.getMsg());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public HttpResult handlerArgument(IllegalArgumentException e,HttpServletRequest request) {
        Map<String, String[]> stringMap = md5ImgParam(new HashMap<>(request.getParameterMap()));
        log.error("\n\t请求IP: {}\n\t请求路径: {}\n\t请求方式: {}\n\t请求参数: {}\n\t自定义异常！:{}",
                request.getRemoteAddr(), request.getRequestURL(), request.getMethod()
                , JSONObject.toJSONString(stringMap), e);
        String msg = e.getMessage();
        return HttpResult.error(ApiErrorEnum.ERROR.getCode(), msg == null ? ApiErrorEnum.ERROR.getMsg() : StringUtil.subBeforeColon(msg));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpResult handler(MethodArgumentNotValidException e,HttpServletRequest  request) {
        Map<String, String[]> stringMap = md5ImgParam(new HashMap<>(request.getParameterMap()));
        log.error("\n\t请求IP: {}\n\t请求路径: {}\n\t请求方式: {}\n\t请求参数: {}\n\t自定义异常！:{}",
                request.getRemoteAddr(), request.getRequestURL(), request.getMethod()
                , JSONObject.toJSONString(stringMap), e);
        return HttpResult.error(ApiErrorEnum.PARAM_T_ERROR.getCode(), e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public HttpResult handler(HttpMessageNotReadableException e) {
        String msg = e.getMessage();
        log.error(e.getMessage(), e);
        return HttpResult.error(ApiErrorEnum.ERROR.getCode(), msg == null ? ApiErrorEnum.ERROR.getMsg() : StringUtil.subBeforeColon(msg));
    }



    @ExceptionHandler(Exception.class)
    public HttpResult handler(Exception e,HttpServletRequest  request) {
        Map<String, String[]> stringMap = md5ImgParam(new HashMap<>(request.getParameterMap()));
        log.error("\n\t请求IP: {}\n\t请求路径: {}\n\t请求方式: {}\n\t请求参数: {}\n\t未知异常！:{}",
                request.getRemoteAddr(), request.getRequestURL(), request.getMethod()
                , JSONObject.toJSONString(stringMap), e);
        String msg = e.getMessage();
        if (e instanceof NullPointerException) {
            msg = ApiErrorEnum.ERROR_NULL_POINT.getMsg();
        }
        return HttpResult.error(ApiErrorEnum.ERROR.getCode(), msg == null ? ApiErrorEnum.ERROR.getMsg() : StringUtil.subBeforeColon(msg));
    }


    private Map<String, String[]> md5ImgParam(Map<String, String[]> parameterMap) {
        String[] imgpaths = parameterMap.get("imgPath");

        String[] list = new String[0];
        if (null != imgpaths && imgpaths.length != 0) {
            List<String> imgpathFinal = new ArrayList<>();
            //获取第一个字符串
            String imgPathStr = imgpaths[0];
            String[] split = imgPathStr.split("\\|");
            for (String s : split) {
                String s1 = MD5Util.md5(s);
                imgpathFinal.add(s1);
            }
            list = imgpathFinal.toArray(new String[split.length]);
        }
        parameterMap.put("imgPath", list);
        return parameterMap;

    }


}
