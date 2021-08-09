package poptional.test.model;

import poptional.OptionalObject;

import java.util.Optional;

@OptionalObject
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

	@OptionalObject.NotNull
	public Address getAddressPlain() {
		return this.address;
	}
}
