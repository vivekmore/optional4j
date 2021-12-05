package optional4j.test.model;

import static org.assertj.core.api.Assertions.assertThat;

import optional4j.spec.Optional;
import org.junit.Test;

public class IsoCodeTest {

    @Test
    public void alphaCode2IsOptional() {

        // given
        IsoCode isoCode = new IsoCode();

        // when
        Optional<AlphaCode2> alphaCode2 = isoCode.getAlphaCode2();

        // then
        assertThat(AlphaCode2.class.isAssignableFrom(alphaCode2.getClass())).isFalse();
        assertThat(Optional.class.isAssignableFrom(alphaCode2.getClass())).isTrue();
    }
}
