package com.petra.ioptional.test.model;

import org.junit.Test;

public class CountryTest {

	@Test
	public void country() {

		Country$ country = new Country$();

		IsoCode$ isoCode = country.getIsoCode()
				.orElse(new IsoCode$())
				.get();
	}
}