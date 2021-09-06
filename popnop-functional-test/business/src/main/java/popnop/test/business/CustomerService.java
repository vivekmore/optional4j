package popnop.test.business;

import popnop.test.model.Address;
import popnop.test.model.Country;
import popnop.test.model.Customer;

import javax.annotation.Nullable;

public class CustomerService {

    @Nullable
    public Customer getCustomer(String customerId) {
        if (customerId.equals("abc")) {
            return new Customer();
        }else if (customerId.equals("efg")) {
            return new Customer(new Address());
        }

        return new Customer(new Address(new Country()));
    }
}