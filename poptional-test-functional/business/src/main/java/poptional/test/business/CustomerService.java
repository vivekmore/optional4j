package poptional.test.business;

import poptional.OptionalType;
import poptional.test.model.Customer;

public class CustomerService {

	@OptionalType
	public Customer getCustomer(String customerId) {
		return new Customer();
	}
}