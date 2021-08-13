package poptional.test.controller;

import poptional.test.business.CustomerService;
import poptional.test.model.Customer;

public class CustomerClient {

    private final CustomerService customerService = new CustomerService();

    public Customer getCustomer(String customerId) {
        return customerService.getCustomer("1234")
                .orElseGet(Customer::new);
    }
}
