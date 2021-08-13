package poptional.test.model;

import poptional.OptionalType;

import javax.annotation.NonNull;

@OptionalType
public class Customer {

	private Address address;

	public Customer(Address address) {
		this.address = address;
	}
	public Customer() {
	}

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@NonNull
	public Address getAddressPlain() {
		return this.address;
	}
}
