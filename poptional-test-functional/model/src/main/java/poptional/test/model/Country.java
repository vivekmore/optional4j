package poptional.test.model;

import poptional.OptionalObject;

@OptionalObject
public class Country {

	private IsoCode isoCode;

	public Country(IsoCode isoCode) {
		this.isoCode = isoCode;
	}

	public Country() {
	}

	public IsoCode getIsoCode() {
		return this.isoCode;
	}

	public void setIsoCode(IsoCode isoCode) {
		this.isoCode = isoCode;
	}

	@OptionalObject.NotNull
	public IsoCode getIsoCodePlain() {
		return this.isoCode;
	}
}

