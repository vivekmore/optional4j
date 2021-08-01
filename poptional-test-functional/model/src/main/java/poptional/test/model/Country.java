package poptional.test.model;

import poptional.OptionalObject;

import java.util.Optional;

@OptionalObject
public class Country {

	private IsoCode isoCode;

	public IsoCode getIsoCode() {
		return this.isoCode;
	}

	@OptionalObject.NotNull
	public IsoCode getIsoCodePlain() {
		return this.isoCode;
	}

	public Optional<IsoCode> getIsoCodeOptional() {
		return Optional.ofNullable(this.isoCode);
	}

	public gopt.Goptional<IsoCode> getIsoCodeGuavaOptional() {
		return gopt.Goptional.fromNullable(isoCode);
	}

	public void setIsoCode(IsoCode isoCode) {
		this.isoCode = isoCode;
	}
}

