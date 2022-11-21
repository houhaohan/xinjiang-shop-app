package com.pinet.core.http;

import java.io.Serializable;

/**
 * HTTP结果封装
 */
public class HttpResult<T> implements Serializable {

    private int code = 200;
    private boolean isSuccess = true;
    private String msg;
    private T data;

    public static <T> HttpResult<T> error() {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
    }

    public static <T> HttpResult<T> error(String msg) {
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static <T> HttpResult<T> error(int code, String msg, T data) {
        HttpResult<T> r = new HttpResult<>();
        r.setCode(code);
        r.setMsg(msg);
        if(data!=null)
        r.setData(data);
        r.setSuccess(false);
        return r;
    }

    public static <T> HttpResult<T> error(int code, String msg) {
        return error(code, msg, null);
    }


    public static <T> HttpResult<T> ok(int code, String msg, T data) {
        HttpResult<T> r = new HttpResult<>();
        r.setCode(code);
        r.setMsg(msg);
        if(data!=null)
        r.setData(data);
        return r;
    }

    public static <T> HttpResult<T> ok(String msg) {
        return ok(HttpStatus.SC_OK, msg, null);
    }

    public static <T> HttpResult<T> ok(String msg, T data) {
        return ok(HttpStatus.SC_OK, msg, data);
    }

    public static <T> HttpResult<T> ok(T data) {
        return ok(HttpStatus.SC_OK, "处理成功", data);
    }

    public static <T> HttpResult<T> ok() {
        return ok("处理成功");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        this.isSuccess = success;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (msg == null ? 0 : msg.hashCode());
        result = prime * result + (data == null ? 0 : data.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof HttpResult)) {
            return false;
        } else {
            HttpResult<?> o = (HttpResult) obj;
            if (o.getCode() == this.getCode() && (o.getMsg() != null && o.getMsg().equals(this.getMsg())) && (o.getData() == null && o.getData() == this.getData() || o.getData().hashCode() == this.getData().hashCode())) {
                return true;
            }
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("HttpResult返回结果:( ");
        if (code > 0)
            sb.append("code:").append(this.code).append(" , ");
        if (msg != null)
            sb.append("msg:").append(this.msg).append(" , ");
        if (data != null)
            sb.append("data:").append(this.data).append(" , ");
        return sb.toString();
    }
}
