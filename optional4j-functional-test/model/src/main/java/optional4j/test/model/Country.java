package optional4j.test.model;

import optional4j.annotation.ValueType;

import javax.annotation.Nullable;

@ValueType
public class Country {

	private IsoCode isoCode;

	public Country(IsoCode isoCode) {
		this.isoCode = isoCode;
	}

	public Country() {
	}

	@Nullable
	public IsoCode getIsoCode() {
		return this.isoCode;
	}

	public void setIsoCode(IsoCode isoCode) {
		this.isoCode = isoCode;
	}

	public IsoCode getIsoCodePlain() {
		return this.isoCode;
	}
}

