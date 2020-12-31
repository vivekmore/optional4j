package com.petra.ioptional.test.model;

import com.petra.ioptional.annotations.Optionalizable;

@Optionalizable
 public class Country {

	private IsoCode isoCode;

	public IsoCode getIsoCode() {
		return this.isoCode;
	}

	public void setIsoCode(IsoCode isoCode) {
		this.isoCode = isoCode;
	}
}

