package optional4j.test.model;

import javax.annotation.Nullable;
import optional4j.annotation.ValueType;

@ValueType
public class Customer {

    private Address address;

    public Customer(Address address) {
        this.address = address;
    }

    public Customer() {}

    @Nullable
    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddressPlain() {
        return this.address;
    }
}
