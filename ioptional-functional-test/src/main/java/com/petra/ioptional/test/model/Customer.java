package com.petra.ioptional.test.model;

import com.petra.ioptional.annotations.Optionalizable;

@Optionalizable
 public class Customer {

	private String firstName;

	private String lastName;

	private String position;

	private Address address1;

	private Address address2;

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getPosition() {
		return this.position;
	}

	public Address getAddress1() {
		return this.address1;
	}

	public Address getAddress2() {
		return this.address2;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setAddress1(Address address1) {
		this.address1 = address1;
	}

	public void setAddress2(Address address2) {
		this.address2 = address2;
	}
}
