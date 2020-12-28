package com.petra.ioptional.test.model;

import com.petra.ioptional.annotations.Optionalizable;

import java.util.UUID;

@Optionalizable
 class Order {

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
