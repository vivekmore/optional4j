package poptional.test.model;

import poptional.OptionalObject;

import java.util.UUID;

@OptionalObject
public class Order {

	private UUID orderId;

	private Customer customer;

	public UUID getOrderId() {
		return this.orderId;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setOrderId(UUID orderId) {
		this.orderId = orderId;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}
