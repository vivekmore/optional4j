package optional4j.test.model;

import static org.assertj.core.api.Assertions.assertThat;

import optional4j.spec.Optional;
import org.junit.Test;

public class AlphaCode2Test {

    @Test
    public void yearIsOptional() {

        // given
        AlphaCode2 alphaCode2 = new AlphaCode2();

        // when
        Optional<Year> year = alphaCode2.getYear();

        // then
        assertThat(Year.class.isAssignableFrom(year.getClass())).isFalse();
        assertThat(Optional.class.isAssignableFrom(year.getClass())).isTrue();
    }
}
