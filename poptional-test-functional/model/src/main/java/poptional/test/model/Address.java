package poptional.test.model;

import org.apache.commons.math3.analysis.function.Add;
import poptional.OptionalType;

import javax.annotation.NonNull;

@OptionalType
public class Address {

    private Country country;

    public Address(Country country) {
        this.country = country;
    }

    public Address() {
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @NonNull
    public Country getCountryPlain() {
        return this.country;
    }
}
