package popnop.test.controller;

import popnop.test.business.CustomerService;
import popnop.test.model.Customer;

public class CustomerClient {

    private final CustomerService customerService = new CustomerService();

    public Customer getCustomer(String customerId) {
        return customerService.getCustomer("1234")
                .orElseGet(Customer::new);
    }
}
