package optional4j.test.model;

import javax.annotation.Nullable;
import optional4j.annotation.Optional4J;

@Optional4J
public class Order implements CustomerProvider {

    private Customer customer;

    public Order(Customer customer) {
        this.customer = customer;
    }

    public Order() {}

    @Nullable
    public Customer getCustomer() {
        return this.customer;
    }

    public Customer getCustomerPlain() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
