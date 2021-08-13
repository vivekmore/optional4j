package poptional.test.model;

import poptional.OptionalType;

import javax.annotation.NonNull;

@OptionalType
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

	@NonNull
	public IsoCode getIsoCodePlain() {
		return this.isoCode;
	}
}

