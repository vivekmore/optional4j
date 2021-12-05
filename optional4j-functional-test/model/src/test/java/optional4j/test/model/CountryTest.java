package optional4j.test.model;

import static org.assertj.core.api.Assertions.assertThat;

import optional4j.spec.Optional;
import org.junit.Test;

public class CountryTest {

    @Test
    public void isoCodeIsOptional() {

        // given
        Country country = new Country();

        // when
        Optional<IsoCode> isoCode = country.getIsoCode();

        // then
        assertThat(IsoCode.class.isAssignableFrom(isoCode.getClass())).isFalse();
        assertThat(Optional.class.isAssignableFrom(isoCode.getClass())).isTrue();
    }
}
