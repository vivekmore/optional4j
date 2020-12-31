package com.petra.ioptional.test.model;

import com.petra.ioptional.annotations.Optionalizable;

@Optionalizable
 public class IsoCode {

	private Integer code;

	public Integer getCode() {
		return this.code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}

