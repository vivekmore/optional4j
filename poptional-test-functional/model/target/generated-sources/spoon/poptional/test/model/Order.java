package poptional.test.model;
import poptional.OptionalObject;
import poptional.Poptional;
import poptional.Something;
@OptionalObject
public final class Order extends Something<Order> {
    private Customer customer;

    public Order(Customer customer) {
        this.customer = customer;
    }

    public Order() {
    }

    @OptionalObject.NotNull
    public Customer getCustomerPlain() {
        return this.customer;
    }

    public Poptional<Customer> getCustomer() {
        return Poptional.ofNullable(this.customer);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}