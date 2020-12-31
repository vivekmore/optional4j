package com.petra.ioptional.test.model;

import com.petra.ioptional.annotations.Optionalizable;

@Optionalizable
 public class Address {

	private Country country;

	private String street;

	private String city;

	private String zipcode;

	public Country getCountry() {
		return this.country;
	}

	public String getStreet() {
		return this.street;
	}

	public String getCity() {
		return this.city;
	}

	public String getZipcode() {
		return this.zipcode;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
}
