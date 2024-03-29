package optional4j.test.model;

import javax.annotation.Nullable;
import optional4j.annotation.NullAssert;
import optional4j.annotation.Optional4J;

@Optional4J
public class Customer implements AddressProvider {

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

        //        @Optional4J
        //        Integer value =
        //                new Customer()
        //                        .getAddressPlain()
        //                        .getCountryPlain()
        //                        .getIsoCodePlain()
        //                        .getCodePlain()
        //                        .getYearPlain()
        //                        .getValue()
        //                        .orElse(0);
        //
        //        return value;
        return 0;
    }

    public void nullAssertTest() {

        //        @NullAssert
        //        Integer value =
        //                new Customer()
        //                        .getAddressPlain()
        //                        .getCountryPlain()
        //                        .getIsoCodePlain()
        //                        .getCodePlain()
        //                        .getYearPlain()
        //                        .getValue()
        //                        .orElse(0);
    }

    public Address nullSafeTest2() {

        @Optional4J Address address = new Customer().getAddressPlain();

        return address;
    }

    public void nullAssertTest2() {

        @NullAssert Address address = new Customer().getAddressPlain();
    }

    public Customer nullSafeTest3() {

        @Optional4J Customer customer = doGetCustomer();

        return customer;
    }

    public void nullAssertTest3() {

        @NullAssert Customer customer = doGetCustomer();
    }

    public Address nullSafeTest4() {

        @Optional4J Address address = doGetCustomer().getAddressPlain();

        return address;
    }

    public void nullAssertTest4() {

        @NullAssert Address address = doGetCustomer().getAddressPlain();
    }

    public Customer nullSafeTest5() {

        @Optional4J Customer customer = doGetCustomerStatically();

        return customer;
    }

    public void nullAssertTest5() {

        @NullAssert Customer customer = doGetCustomerStatically();
    }

    public Address nullSafeTest6() {

        @Optional4J Address address = doGetCustomerStatically().getAddressPlain();

        return address;
    }

    public Address nullSafeTest7() {

        @Optional4J Address address = new Customer(new Address(new Country())).getAddressPlain();

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

    public Year nullSafeTest8() {

        @Optional4J
        Year year =
                new Customer()
                        .getAddressPlain()
                        .getCountryPlain()
                        .getIsoCodePlain()
                        .getCodePlain()
                        .getYearPlain();
        return year;
    }

    private Customer doGetCustomer() {
        return new Customer();
    }

    private static Customer doGetCustomerStatically() {
        return new Customer();
    }
}
