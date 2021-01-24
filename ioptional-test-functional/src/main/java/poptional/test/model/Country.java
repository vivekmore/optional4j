package poptional.test.model;

import poptional.OptionalObject;

@OptionalObject
public class Country {

	private IsoCode isoCode;

	public IsoCode getIsoCode() {
		return this.isoCode;
	}

	public void setIsoCode(IsoCode isoCode) {
		this.isoCode = isoCode;
	}
}

