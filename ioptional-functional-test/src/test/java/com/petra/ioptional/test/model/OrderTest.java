package com.petra.ioptional.test.model;

import org.junit.Test;

public class OrderTest {

	@Test
	public void order() {

		Order$ order = new Order$();

		Customer$ customer = order.getCustomer()
				.orElse(new Customer$());
	}
}