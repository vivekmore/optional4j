package popnop.test.model;

import popnop.spec.NullObjectType;
import javax.annotation.NonNull;

@NullObjectType
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

