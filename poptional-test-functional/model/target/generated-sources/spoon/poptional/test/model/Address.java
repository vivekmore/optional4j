package poptional.test.model;
import poptional.OptionalObject;
import poptional.Poptional;
import poptional.Something;
@OptionalObject
public final class Address extends Something<Address> {
    private Country country;

    public Address(Country country) {
        this.country = country;
    }

    public Address() {
    }

    public Poptional<Country> getCountry() {
        return Poptional.ofNullable(country);
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @OptionalObject.NotNull
    public Country getCountryPlain() {
        return this.country;
    }
}