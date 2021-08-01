package poptional.performance;

import lombok.experimental.UtilityClass;
import poptional.test.model.Address;
import poptional.test.model.Code;
import poptional.test.model.Country;
import poptional.test.model.Customer;
import poptional.test.model.IsoCode;
import poptional.test.model.Order;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@UtilityClass
public class Util {

	public static final int WARM_UP_ITERATIONS = 2;
	public static final int MEASUREMENT_ITERATIONS = 2;
	public static final int FORK_VALUE = 1;
	public static final Random RANDOM = new Random(System.currentTimeMillis());

	public static Order[] createOrders(int size) {
		Order[] orders = new Order[size];
		for (int i = 0; i < size; i++) {
			orders[i] = newOrder();
		}
		return orders;
	}

	public static Map<Integer, Order> createOrdersMap(int size) {
		Map<Integer, Order> orders = new HashMap<>();
		for (int i = 0; i < size; i++) {
			orders.put(i, newOrder());
		}
		return orders;
	}

	public static Order newOrder() {
		Order order = new Order();
		order.setOrderId(UUID.randomUUID());
		order.setCustomer(newCustomer());
		return order;
	}

	public static Customer newCustomer() {
		Customer customer = new Customer();
		customer.setFirstName("Saleh" + RANDOM.nextInt(99999));
		customer.setLastName("Amareen" + RANDOM.nextInt(99999));
		customer.setPosition("One cool developer" + RANDOM.nextInt(99999));
		customer.setAddress1(newAddress());
		customer.setAddress2(newAddress());
		return customer;
	}

	public static Address newAddress() {
		Address address = new Address();
		address.setStreet(RANDOM.nextInt(99999) + " Barclay");
		address.setCity("Ann Arbor" + RANDOM.nextInt());
		address.setZipcode(RANDOM.nextInt(99999) + "");
		address.setCountry(newCountry());
		return address;
	}

	public static Country newCountry() {
		Country country = new Country();
		country.setIsoCode(newIsoCode());
		return country;
	}

	public static IsoCode newIsoCode() {
		IsoCode isoCode = new IsoCode();
		isoCode.setCode(newCode());
		return isoCode;
	}

	public static Code newCode() {
		Code code = new Code();
		code.setCode(RANDOM.nextInt(99999));
		return code;
	}
}
