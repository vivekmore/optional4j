package com.petra.ioptional;

import com.petra.ioptional.test.model.Address;
import com.petra.ioptional.test.model.Address$;
import com.petra.ioptional.test.model.Country;
import com.petra.ioptional.test.model.Country$;
import com.petra.ioptional.test.model.Customer;
import com.petra.ioptional.test.model.Customer$;
import com.petra.ioptional.test.model.IsoCode;
import com.petra.ioptional.test.model.IsoCode$;
import com.petra.ioptional.test.model.Order;
import com.petra.ioptional.test.model.Order$;
import lombok.experimental.UtilityClass;

import java.util.Random;
import java.util.UUID;

@UtilityClass
public class Util {

	public static final int WARM_UP_ITERATIONS = 2;
	public static final int MEASUREMENT_ITERATIONS = 2;
	public static final int FORK_VALUE = 1;
	public static final Random RANDOM = new Random(System.currentTimeMillis());

	public static Order$ newOrder$() {
		Order$ order$ = new Order$();
		order$.setOrderId(UUID.randomUUID());
		order$.setCustomer(newCustomer$());
		return order$;
	}

	public static Order newOrder() {
		Order order = new Order();
		order.setOrderId(UUID.randomUUID());
		order.setCustomer(newCustomer());
		return order;
	}

	private static Customer$ newCustomer$() {
		Customer$ customer$ = new Customer$();
		customer$.setFirstName("Saleh" + RANDOM.nextInt(99999));
		customer$.setLastName("Amareen" + RANDOM.nextInt(99999));
		customer$.setPosition("One cool developer" + RANDOM.nextInt(99999));
		customer$.setAddress1(newAddress$());
		return customer$;
	}

	private static Customer newCustomer() {
		Customer customer = new Customer();
		customer.setFirstName("Saleh" + RANDOM.nextInt(99999));
		customer.setLastName("Amareen" + RANDOM.nextInt(99999));
		customer.setPosition("One cool developer" + RANDOM.nextInt(99999));
		customer.setAddress1(newAddress());
		return customer;
	}

	private static Address$ newAddress$() {
		Address$ address$ = new Address$();
		address$.setStreet(RANDOM.nextInt(99999) + " Barclay");
		address$.setCity("Ann Arbor" + RANDOM.nextInt());
		address$.setZipcode(RANDOM.nextInt(99999) + "");
		address$.setCountry(newCountry$());
		return address$;
	}

	private static Address newAddress() {
		Address address = new Address();
		address.setStreet(RANDOM.nextInt(99999) + " Barclay");
		address.setCity("Ann Arbor" + RANDOM.nextInt());
		address.setZipcode(RANDOM.nextInt(99999) + "");
		address.setCountry(newCountry());
		return address;
	}

	private static Country$ newCountry$() {
		Country$ country$ = new Country$();
		country$.setIsoCode(newIsoCode$());
		return country$;
	}

	private static Country newCountry() {
		Country country = new Country();
		country.setIsoCode(newIsoCode());
		return country;
	}

	private static IsoCode$ newIsoCode$() {
		IsoCode$ isoCode$ = new IsoCode$();
		isoCode$.setCode(RANDOM.nextInt(99999));
		return isoCode$;
	}

	private static IsoCode newIsoCode() {
		IsoCode isoCode = new IsoCode();
		isoCode.setCode(RANDOM.nextInt(99999));
		return isoCode;
	}
}
