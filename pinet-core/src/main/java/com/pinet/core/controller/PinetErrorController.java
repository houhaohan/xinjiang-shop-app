package com.pinet.core.controller;

import com.pinet.core.http.HttpStatus;
import com.pinet.core.ApiErrorEnum;
import com.pinet.core.exception.PinetException;
import com.pinet.core.result.Result;
import com.pinet.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Slf4j
@RestController
@RestControllerAdvice
public class PinetErrorController implements ErrorController {
    @RequestMapping("/error")
    public Result error(HttpServletRequest request, HttpServletResponse response) {
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
            return Result.error(uguessException.getCode(), msg);
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
        return Result.error(response.getStatus(), StringUtil.isEmpty(msg) ? "系统发生未知的错误" : msg);
    }


    /**
     * 处理自定义异常
     */
    @ExceptionHandler(PinetException.class)
    public Result handler(PinetException e) {
        log.error(e.getMessage(), e);
        return Result.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result handlerArgument(IllegalArgumentException e) {
        log.error("系统出现异常",e);
        String msg = e.getMessage();
        return Result.error(ApiErrorEnum.ERROR.getCode(), msg == null ? ApiErrorEnum.ERROR.getMsg() : StringUtil.subBeforeColon(msg));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,BindException.class})
    public Result handler(BindException e) {
        log.error(e.getMessage());
        return Result.error(ApiErrorEnum.PARAM_T_ERROR.getCode(), e.getBindingResult().getFieldError().getDefaultMessage());
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handler(HttpMessageNotReadableException e) {
        String msg = e.getMessage();
        log.error(e.getMessage(), e);
        return Result.error(ApiErrorEnum.ERROR.getCode(), msg == null ? ApiErrorEnum.ERROR.getMsg() : StringUtil.subBeforeColon(msg));
    }



    @ExceptionHandler(Exception.class)
    public Result handler(Exception e) {
        log.error("系统出现异常",e);
        String msg = e.getMessage();
        if (e instanceof NullPointerException) {
            msg = ApiErrorEnum.ERROR_NULL_POINT.getMsg();
        }
        return Result.error(ApiErrorEnum.ERROR.getCode(), msg == null ? ApiErrorEnum.ERROR.getMsg() : StringUtil.subBeforeColon(msg));
    }


}
