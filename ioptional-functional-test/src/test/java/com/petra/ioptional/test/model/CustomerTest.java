package com.petra.ioptional.test.model;

import org.junit.Test;

public class CustomerTest {

	@Test
	public void customer() {

		Customer$ customer = new Customer$();

		Address$ address1 = customer.getAddress1()
				.orElse(new Address$());

		Address$ address2 = customer.getAddress2()
				.orElse(new Address$());
	}
}