package ioptional.test.model;

import ioptional.OptionalReturn;
import ioptional.OptionalType;

@OptionalType
@OptionalReturn
public class Address {

	private Country country;

	private String street;

	private String city;

	private String zipcode;

	public Country getCountry() {
		return country;
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
