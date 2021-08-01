package poptional.test.model;

import poptional.OptionalObject;

import java.util.Optional;

@OptionalObject
public class Address {

	private Country country;

	private String street;

	private String city;

	private String zipcode;

	public Country getCountry() {
		return country;
	}

	@OptionalObject.NotNull
	public Country getCountryPlain() {
		return this.country;
	}
	
	public Optional<Country> getCountryOptional() {
		return Optional.ofNullable(country);
	}

	public gopt.Goptional<Country> getCountryGuavaOptional() {
		return gopt.Goptional.fromNullable(country);
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
}
