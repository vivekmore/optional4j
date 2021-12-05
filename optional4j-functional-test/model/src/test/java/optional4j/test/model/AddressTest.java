package optional4j.test.model;

import static org.assertj.core.api.Assertions.assertThat;

import optional4j.spec.Optional;
import org.junit.Test;

public class AddressTest {

    @Test
    public void countryIsOptional() {

        // given
        Address address = new Address();

        // when
        Optional<Country> country = address.getCountry();

        // then
        assertThat(Country.class.isAssignableFrom(country.getClass())).isFalse();
        assertThat(Optional.class.isAssignableFrom(country.getClass())).isTrue();
    }

    @Test
    public void zipcodeIsAnInteger() {

        // given
        Address address = new Address();

        // when
        Integer zipcode = address.getZipcode();

        // then
        assertThat(Integer.class.isAssignableFrom(zipcode.getClass())).isTrue();
        assertThat(Optional.class.isAssignableFrom(zipcode.getClass())).isFalse();
    }

    @Test
    public void streetIsNullObject() {

        // given
        Address address = new Address();

        // when
        AbstractStreet street = address.getStreet();

        // then
        assertThat(Street.class.isAssignableFrom(street.getClass())).isFalse();
        assertThat(AbstractStreet.class.isAssignableFrom(street.getClass())).isTrue();
        assertThat(Optional.class.isAssignableFrom(street.getClass())).isTrue();
    }
}
