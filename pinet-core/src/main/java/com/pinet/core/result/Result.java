package com.pinet.core.result;

import java.io.Serializable;
import com.pinet.core.http.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *   接口返回数据格式
 * @author scott
 * @email jeecgos@163.com
 * @date  2019年1月19日
 */
@Data
@ApiModel(value="接口返回对象", description="接口返回对象")
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 成功标志
	 */
	@ApiModelProperty(value = "成功标志")
	private boolean success = true;

	/**
	 * 返回处理消息
	 */
	@ApiModelProperty(value = "返回处理消息")
	private String message = "操作成功！";

	/**
	 * 返回代码
	 */
	@ApiModelProperty(value = "返回代码")
	private Integer code = 0;
	
	/**
	 * 返回数据对象 data
	 */
	@ApiModelProperty(value = "返回数据对象")
	private T result;
	
	/**
	 * 时间戳
	 */
	@ApiModelProperty(value = "时间戳")
	private long timestamp = System.currentTimeMillis();

	public Result() {
		
	}

	public Result(Integer code, String message, T result) {
		if(!code.equals(200) ){
			this.success = false;
		}
		this.message = message;
		this.code = code;
		this.result = result;
	}

	public Result<T> success(String message) {
		this.message = message;
		this.code = HttpStatus.SC_OK;
		this.success = true;
		return this;
	}
	
	
	public static Result<Object> ok() {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(HttpStatus.SC_OK);
		r.setMessage("成功");
		return r;
	}
	
	public static Result<Object> ok(String msg) {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(HttpStatus.SC_OK);
		r.setMessage(msg);
		return r;
	}
	
	public static Result<Object> ok(Object data) {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(HttpStatus.SC_OK);
		r.setResult(data);
		return r;
	}

	public static Result<Object> ok(String msg,Object data) {
		Result<Object> r = new Result<Object>();
		r.setSuccess(true);
		r.setCode(HttpStatus.SC_OK);
		r.setMessage(msg);
		r.setResult(data);
		return r;
	}
	
	public static Result<Object> error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}
	
	public static Result<Object> error(int code, String msg) {
		Result<Object> r = new Result<Object>();
		r.setCode(code);
		r.setMessage(msg);
		r.setSuccess(false);
		return r;
	}

	public static Result<Object> error(String msg,Object data) {
		Result<Object> r = error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
		r.setResult(data);
		return r;
	}

	public static Result<Object> error(int code, String msg,Object data) {
		Result<Object> r = new Result<Object>();
		r.setCode(code);
		r.setMessage(msg);
		r.setResult(data);
		r.setSuccess(false);
		return r;
	}

	public Result<T> error500(String message) {
		this.message = message;
		this.code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
		this.success = false;
		return this;
	}

}