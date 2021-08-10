package poptional.test.model;

import poptional.OptionalObject;

@OptionalObject
public class Address {

	private Country country;

	public Address(Country country) {
		this.country = country;
	}

	public Address() {
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@OptionalObject.NotNull
	public Country getCountryPlain() {
		return this.country;
	}
}
