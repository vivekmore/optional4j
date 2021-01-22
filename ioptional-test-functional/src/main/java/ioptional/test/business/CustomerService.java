package ioptional.test.business;

import ioptional.test.model.Customer;
import ioptional.OptionalReturn;

@OptionalReturn
public class CustomerService {

	public Customer getCustomer(String customerId) {
		return new Customer();
	}
}
