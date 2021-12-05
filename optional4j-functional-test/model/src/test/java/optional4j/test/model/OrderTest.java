package optional4j.test.model;

import static org.assertj.core.api.Assertions.assertThat;

import optional4j.spec.Optional;
import org.junit.Test;

public class OrderTest {

    @Test
    public void customerIsOptional() {

        // given
        Order order = new Order();

        // when
        Optional<Customer> customer = order.getCustomer();

        // then
        assertThat(Customer.class.isAssignableFrom(customer.getClass())).isFalse();
        assertThat(Optional.class.isAssignableFrom(customer.getClass())).isTrue();
    }
}
