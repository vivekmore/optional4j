package poptional.performance;

import poptional.Poptional;
import poptional.test.model.Address;
import poptional.test.model.Code;
import poptional.test.model.Country;
import poptional.test.model.Customer;
import poptional.test.model.IsoCode;
import poptional.test.model.Order;

import java.util.Optional;

public class ModelUtil {

	static final Order ORDER = new Order();
	static final Customer CUSTOMER = new Customer();
	static final Address ADDRESS1 = new Address();
	static final Country COUNTRY = new Country();
	static final IsoCode ISO_CODE = new IsoCode();
	static final Code CODE = new Code();

	//////// IfNull /////////

	public static Order getOrder(Order order) {

		if (order != null) {
			return order;
		}
		return ORDER;
	}

	public static Customer getCustomer(Order order) {

		if (order == null) {
			return CUSTOMER;
		}
		if (order.getCustomerPlain() == null) {
			return CUSTOMER;
		}
		return order.getCustomerPlain();
	}

	public static Address getAddress1(Order order) {

		if (order == null) {
			return ADDRESS1;
		}
		if (order.getCustomerPlain() == null) {
			return ADDRESS1;
		}

		if (order.getCustomerPlain()
				.getAddress1Plain() == null) {
			return ADDRESS1;
		}

		return order.getCustomerPlain()
				.getAddress1Plain();
	}

	public static Country getCountry(Order order) {

		if (order == null) {
			return COUNTRY;
		}
		if (order.getCustomerPlain() == null) {
			return COUNTRY;
		}

		if (order.getCustomerPlain()
				.getAddress1Plain() == null) {
			return COUNTRY;
		}

		if (order.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain() == null) {
			return COUNTRY;
		}

		return order.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain();
	}

	public static IsoCode getIsoCode(Order order) {

		if (order == null) {
			return ISO_CODE;
		}
		if (order.getCustomerPlain() == null) {
			return ISO_CODE;
		}

		if (order.getCustomerPlain()
				.getAddress1Plain() == null) {
			return ISO_CODE;
		}

		if (order.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain() == null) {
			return ISO_CODE;
		}

		if (order.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain()
				.getIsoCodePlain() == null) {
			return ISO_CODE;
		}

		return order.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain()
				.getIsoCodePlain();
	}

	public static Code getCode(Order order) {

		if (order == null) {
			return CODE;
		}
		if (order.getCustomerPlain() == null) {
			return CODE;
		}

		if (order.getCustomerPlain()
				.getAddress1Plain() == null) {
			return CODE;
		}

		if (order.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain() == null) {
			return CODE;
		}

		if (order.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain()
				.getIsoCodePlain() == null) {
			return CODE;
		}

		if (order.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain()
				.getIsoCodePlain()
				.getCodePlain() == null) {
			return CODE;
		}

		return order.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain()
				.getIsoCodePlain()
				.getCodePlain();
	}

	//////// Poptional /////////

	public static Order getOrderPoptional(Order order) {

		return Poptional.ofNullable(order)
				.orElse(ORDER);
	}

	public static Customer getCustomerPoptional(Order order) {

		return Poptional.ofNullable(order)
				.flatMap(Order::getCustomer)
				.orElse(CUSTOMER);
	}

	public static Address getAddress1Poptional(Order order) {

		return Poptional.ofNullable(order)
				.flatMap(Order::getCustomer)
				.flatMap(Customer::getAddress1)
				.orElse(ADDRESS1);
	}

	public static Country getCountryPoptional(Order order) {

		return Poptional.ofNullable(order)
				.flatMap(Order::getCustomer)
				.flatMap(Customer::getAddress1)
				.flatMap(Address::getCountry)
				.orElse(COUNTRY);
	}

	public static IsoCode getIsoCodePoptional(Order order) {

		return Poptional.ofNullable(order)
				.flatMap(Order::getCustomer)
				.flatMap(Customer::getAddress1)
				.flatMap(Address::getCountry)
				.flatMap(Country::getIsoCode)
				.orElse(ISO_CODE);
	}

	public static Code getCodePoptional(Order order) {

		return Poptional.ofNullable(order)
				.flatMap(Order::getCustomer)
				.flatMap(Customer::getAddress1)
				.flatMap(Address::getCountry)
				.flatMap(Country::getIsoCode)
				.flatMap(IsoCode::getCode)
				.orElse(CODE);
	}

	//////// Optional /////////

	public static Order getOrderOptional(Order order) {

		return Optional.ofNullable(order)
				.orElse(ORDER);
	}

	public static Customer getCustomerOptional(Order order) {

		return Optional.ofNullable(order)
				.flatMap(Order::getCustomerOptional)
				.orElse(CUSTOMER);
	}

	public static Address getAddressOptional(Order order) {

		return Optional.ofNullable(order)
				.flatMap(Order::getCustomerOptional)
				.flatMap(Customer::getAddress1Optional)
				.orElse(ADDRESS1);
	}

	public static Country getCountryOptional(Order order) {

		return Optional.ofNullable(order)
				.flatMap(Order::getCustomerOptional)
				.flatMap(Customer::getAddress1Optional)
				.flatMap(Address::getCountryOptional)
				.orElse(COUNTRY);
	}

	public static IsoCode getIsoCodeOptional(Order order) {

		return Optional.ofNullable(order)
				.flatMap(Order::getCustomerOptional)
				.flatMap(Customer::getAddress1Optional)
				.flatMap(Address::getCountryOptional)
				.flatMap(Country::getIsoCodeOptional)
				.orElse(ISO_CODE);
	}

	public static Code getCodeOptional(Order order) {

		return Optional.ofNullable(order)
				.flatMap(Order::getCustomerOptional)
				.flatMap(Customer::getAddress1Optional)
				.flatMap(Address::getCountryOptional)
				.flatMap(Country::getIsoCodeOptional)
				.flatMap(IsoCode::getCodeOptional)
				.orElse(CODE);
	}
}
