package poptional.test.model;
import poptional.OptionalObject;
import poptional.Poptional;
import poptional.Something;
@OptionalObject
public final class Customer extends Something<Customer> {
    private Address address;

    public Customer(Address address) {
        this.address = address;
    }

    public Customer() {
    }

    public Poptional<Address> getAddress() {
        return (this.address) == null? poptional.Poptional.empty(): this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @OptionalObject.NotNull
    public Address getAddressPlain() {
        return this.address;
    }
}