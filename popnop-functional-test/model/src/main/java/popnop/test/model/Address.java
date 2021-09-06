package popnop.test.model;

import popnop.spec.NullObjectType;

import javax.annotation.NonNull;

@NullObjectType
public class Address {

    private Country country;

    private Integer zipcode;

    public Address(Country country) {
        this.country = country;
    }

    public Address() {
    }

    public Country getCountry() {
        return country;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @NonNull
    public Country getCountryPlain() {
        return this.country;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }
}
