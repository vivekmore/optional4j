package com.petra.ioptional.test.model;

import com.petra.ioptional.lang.IOptional;
import org.junit.Test;

public class AddressTest {

	@Test
	public void address() {

		Address$ address = new Address$();

		Integer code = address.getCountry()
				.orElse(new Country$())
				.getIsoCode()
				.orElse(new IsoCode$())
				.getCode();
	}
}