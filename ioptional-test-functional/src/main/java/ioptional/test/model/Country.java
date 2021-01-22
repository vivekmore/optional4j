package ioptional.test.model;

import ioptional.OptionalReturn;
import ioptional.OptionalType;

@OptionalType
@OptionalReturn
 public class Country {

	private IsoCode isoCode;

	public IsoCode getIsoCode() {
		return this.isoCode;
	}

	public void setIsoCode(IsoCode isoCode) {
		this.isoCode = isoCode;
	}
}

