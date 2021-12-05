package optional4j.test.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import optional4j.spec.Optional;
import org.junit.Test;

public class CustomerTest {

    @Test
    public void addressIsOptional() {

        // given
        Customer customer = new Customer();

        // when
        Optional<Address> address = customer.getAddress();

        // then
        assertThat(Address.class.isAssignableFrom(address.getClass())).isFalse();
        assertThat(Optional.class.isAssignableFrom(address.getClass())).isTrue();
    }

    @Test
    public void name() {

        // given
        Customer customer = new Customer();

        // when
        Optional<Address> address = customer.getAddress();

        // then
        assertThat(Address.class.isAssignableFrom(address.getClass())).isFalse();
        assertThat(Optional.class.isAssignableFrom(address.getClass())).isTrue();
    }

    @Test
    public void nullAssertTest() {

        Customer customer = new Customer();

        assertThatExceptionOfType(NullPointerException.class).isThrownBy(customer::nullAssertTest);
    }

    @Test
    public void nullAssertTest2() {

        Customer customer = new Customer();

        customer.nullAssertTest2();
    }

    @Test
    public void nullAssertTest3() {

        Customer customer = new Customer();

        customer.nullAssertTest3();
    }

    @Test
    public void nullAssertTest4() {

        Customer customer = new Customer();

        customer.nullAssertTest4();
    }

    @Test
    public void nullAssertTest5() {

        Customer customer = new Customer();

        customer.nullAssertTest5();
    }

    @Test
    public void nullAssertTest6() {

        Customer customer = new Customer();

        customer.nullAssertTest6();
    }

    @Test
    public void nullAssertTest7() {

        Customer customer = new Customer();

        Address address = customer.nullAssertTest7();

        assertThat(address).isNotNull();
    }

    @Test
    public void nullSafeTest() {

        Customer customer = new Customer();

        assertThat(customer.nullSafeTest()).isNull();
    }

    @Test
    public void nullSafeTest2() {

        Customer customer = new Customer();

        assertThat(customer.nullSafeTest2()).isNull();
    }

    @Test
    public void nullSafeTest3() {

        Customer customer = new Customer();

        assertThat(customer.nullSafeTest3()).isNotNull();
    }

    @Test
    public void nullSafeTest4() {

        Customer customer = new Customer();

        assertThat(customer.nullSafeTest4()).isNull();
    }

    @Test
    public void nullSafeTest5() {

        Customer customer = new Customer();

        assertThat(customer.nullSafeTest5()).isNotNull();
    }

    @Test
    public void nullSafeTest6() {

        Customer customer = new Customer();

        assertThat(customer.nullSafeTest6()).isNull();
    }

    @Test
    public void nullSafeTest7() {

        Customer customer = new Customer();

        assertThat(customer.nullSafeTest7()).isNotNull();
    }
}
