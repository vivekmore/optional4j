package poptional.test.model;

import poptional.OptionalObject;

@OptionalObject
public class Code {

	private Integer code;

	public Code(Integer code) {
		this.code = code;
	}
	public Code() {
	}

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
