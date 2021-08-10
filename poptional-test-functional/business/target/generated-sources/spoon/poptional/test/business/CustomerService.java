package poptional.test.business;
import poptional.OptionalObject;
import poptional.Poptional;
import poptional.Something;
import poptional.test.model.Customer;
@OptionalObject
public final class CustomerService extends Something<CustomerService> {
    public Poptional<Customer> getCustomer(String customerId) {
        return (new Customer(null)) == null? poptional.Poptional.empty(): new Customer(null);
    }
}