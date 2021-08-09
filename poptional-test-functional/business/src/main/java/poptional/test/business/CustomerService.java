package poptional.test.business;

import poptional.OptionalObject;
import poptional.test.model.Customer;

@OptionalObject
public class CustomerService {

	public Customer getCustomer(String customerId) {
		return new Customer(null);
	}
}