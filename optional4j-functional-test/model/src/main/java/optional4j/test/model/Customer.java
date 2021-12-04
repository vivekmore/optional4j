package optional4j.test.model;

import javax.annotation.Nullable;
import optional4j.annotation.NullAssert;
import optional4j.annotation.NullSafe;
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

    public Integer nullSafeTest() {

        @NullSafe
        Integer value =
                new Customer()
                        .getAddressPlain()
                        .getCountryPlain()
                        .getIsoCodePlain()
                        .getCodePlain()
                        .getYearPlain()
                        .getValue();

        return value;
    }

    public void nullAssertTest() {

        @NullAssert
        Integer value =
                new Customer()
                        .getAddressPlain()
                        .getCountryPlain()
                        .getIsoCodePlain()
                        .getCodePlain()
                        .getYearPlain()
                        .getValue();
    }

    public Address nullSafeTest2() {

        @NullSafe Address address = new Customer().getAddressPlain();

        return address;
    }

    public void nullAssertTest2() {

        @NullAssert Address address = new Customer().getAddressPlain();
    }

    public Customer nullSafeTest3() {

        @NullSafe Customer customer = doGetCustomer();

        return customer;
    }

    public void nullAssertTest3() {

        @NullAssert Customer customer = doGetCustomer();
    }

    public Address nullSafeTest4() {

        @NullSafe Address address = doGetCustomer().getAddressPlain();

        return address;
    }

    public void nullAssertTest4() {

        @NullAssert Address address = doGetCustomer().getAddressPlain();
    }

    public Customer nullSafeTest5() {

        @NullSafe Customer customer = doGetCustomerStatically();

        return customer;
    }

    public void nullAssertTest5() {

        @NullAssert Customer customer = doGetCustomerStatically();
    }

    public Address nullSafeTest6() {

        @NullSafe Address address = doGetCustomerStatically().getAddressPlain();

        return address;
    }

    public Address nullSafeTest7() {

        @NullSafe Address address = new Customer(new Address(new Country())).getAddressPlain();

        return address;
    }

    public void nullAssertTest6() {

        @NullAssert Address address = doGetCustomerStatically().getAddressPlain();
    }

    public Address nullAssertTest7() {

        @NullAssert
        Address address =
                new optional4j.test.model.Customer(new Address(new Country())).getAddressPlain();

        return address;
    }

    private Customer doGetCustomer() {
        return new Customer();
    }

    private static Customer doGetCustomerStatically() {
        return new Customer();
    }
}
