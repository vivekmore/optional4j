package optional4j.test.model;

import static optional4j.support.ModeValue.PESSIMISTIC;

import optional4j.annotation.Collaborator;
import optional4j.annotation.Mode;
import optional4j.annotation.OptionalReturn;
import optional4j.annotation.ValueType;
import optional4j.spec.Optional;

@ValueType
public class Address {

    private Country country;

    private Integer zipcode;

    public Address(Country country) {
        this.country = country;
    }

    public void doSomething(Optional<Country> country) {
        country.ifNull(
                new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("country is null");
                    }
                });
    }

    public Address() {}

    @Collaborator
    @Mode(PESSIMISTIC)
    public Country getCountry() {
        return country;
    }

    @OptionalReturn
    public Optional<Country> dummyGetCountry() {
        return null;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setCountry(@Collaborator Country country) {
        this.country = country;
    }

    public Country getCountryPlain() {
        return this.country;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }
}
