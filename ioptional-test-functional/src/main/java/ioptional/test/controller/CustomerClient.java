package ioptional.test.controller;

import ioptional.test.business.CustomerService;
import ioptional.test.model.Customer;

public class CustomerClient {

	private final CustomerService customerService = new CustomerService();

	public Customer getCustomer(String customerId) {
		return customerService.getCustomer("1234").get();
	}
}
