package com.thinkingdata.response;

import java.io.Serializable;
/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2020/5/21 17:45
 */
public class ResponseData<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;

	private String msg;

	private T data;

	public ResponseData(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public ResponseData(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public ResponseData(ResultEnums resultEnums) {
		this.code = resultEnums.getCode();
		this.msg = resultEnums.getMsg();
	}

	public ResponseData(ResultEnums resultEnums, T data) {
		this.code = resultEnums.getCode();
		this.msg = resultEnums.getMsg();
		this.data = data;
	}

	public ResponseData() {
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
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
}
