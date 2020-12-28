package com.petra.ioptional.test.model;

import com.petra.ioptional.lang.IOptional;
import org.junit.Test;

public class AddressTest {

	@Test
	public void address() {

//		Address addressOld = new Address();
//		IsoCode oldIsoCode = addressOld.getCountry()
//				.getIsoCode();

		Address$ address = new Address$();

		IOptional<IsoCode$> isoCode = address.getCountry()
				.orElse(new Country$())
				.getIsoCode();
	}
}