package optional4j.test.model;

import static optional4j.support.ModeValue.PESSIMISTIC;

import optional4j.annotation.Collaborator;
import optional4j.annotation.Mode;
import optional4j.annotation.Optional4J;
import optional4j.spec.Optional;

@Optional4J
public class Address extends StreetProvider implements CountryProvider {

    private Country country;

    private Integer zipcode = 0;

    private Street street;

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

    @Optional4J
    public Country getCountry() {
        return country;
    }

    public void setCountry(@Collaborator Country country) {
        this.country = country;
    }

    @Optional4J
    public Optional<Country> dummyGetCountry() {
        return null;
    }

    public Integer getZipcode() {
        return zipcode;
    }

    public void setZipcode(Integer zipcode) {
        this.zipcode = zipcode;
    }

    public Country getCountryPlain() {
        return this.country;
    }

    @Collaborator
    @Mode(PESSIMISTIC)
    public Street getStreet() {
        return street;
    }

    public void setStreet(Street street) {
        this.street = street;
    }
}
