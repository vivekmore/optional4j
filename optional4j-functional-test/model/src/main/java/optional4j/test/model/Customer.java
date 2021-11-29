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

    public void nullSafeTest() {

        @NullSafe
        Integer value =
                new Customer()
                        .getAddressPlain()
                        .getCountryPlain()
                        .getIsoCodePlain()
                        .getCodePlain()
                        .getYearPlain()
                        .getValue();
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

    public void nullSafeTest2() {

        @NullSafe Address address = new Customer().getAddressPlain();
    }

    public void nullAssertTest2() {

        @NullAssert Address address = new Customer().getAddressPlain();
    }

    public void nullSafeTest3() {

        @NullSafe Customer customer = doGetCustomer();
    }

    public void nullAssertTest3() {

        @NullAssert Customer customer = doGetCustomer();
    }

    public void nullSafeTest4() {

        @NullSafe Address address = doGetCustomer().getAddressPlain();
    }

    public void nullAssertTest4() {

        @NullAssert Address address = doGetCustomer().getAddressPlain();
    }

    public void nullSafeTest5() {

        @NullSafe Customer customer = doGetCustomerStatically();
    }

    public void nullAssertTest5() {

        @NullAssert Customer customer = doGetCustomerStatically();
    }

    public void nullSafeTest6() {

        @NullSafe Address address = doGetCustomerStatically().getAddressPlain();
    }

    public void nullAssertTest6() {

        @NullAssert Address address = doGetCustomerStatically().getAddressPlain();
    }

    private Customer doGetCustomer() {
        return new Customer();
    }

    private static Customer doGetCustomerStatically() {
        return new Customer();
    }
}
