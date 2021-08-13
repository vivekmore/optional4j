package poptional.test.model;

import poptional.OptionalType;

import javax.annotation.NonNull;

@OptionalType
public class Order {

	private Customer customer;

	public Order(Customer customer) {
		this.customer = customer;
	}

	public Order() {
	}

	@NonNull
	public Customer getCustomerPlain() {
		return this.customer;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
