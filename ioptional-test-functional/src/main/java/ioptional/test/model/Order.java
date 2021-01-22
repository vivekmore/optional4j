package ioptional.test.model;

import ioptional.OptionalReturn;
import ioptional.OptionalType;

import java.util.UUID;

@OptionalType
@OptionalReturn
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
