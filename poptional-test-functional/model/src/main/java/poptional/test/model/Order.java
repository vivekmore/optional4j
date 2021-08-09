package poptional.test.model;

import poptional.OptionalObject;

import java.util.Optional;
import java.util.UUID;

@OptionalObject
public class Order {

	private Customer customer;

	public Order(Customer customer) {
		this.customer = customer;
	}

	public Order() {
	}

	@OptionalObject.NotNull
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
