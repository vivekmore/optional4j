package poptional.performance;

import gopt.Goptional;
import poptional.Poptional;
import poptional.test.model.Address;
import poptional.test.model.Code;
import poptional.test.model.Country;
import poptional.test.model.Customer;
import poptional.test.model.IsoCode;
import poptional.test.model.Order;

import java.util.Optional;
import java.util.function.Function;

public class ModelUtil {

	static final Order DEFAULT_ORDER = Util.newOrder();
	static final Customer DEFAULT_CUSTOMER = Util.newCustomer();
	static final Address DEFAULT_ADDRESS1 = Util.newAddress();
	static final Country DEFAULT_COUNTRY = Util.newCountry();
	static final IsoCode DEFAULT_ISO_CODE = Util.newIsoCode();
	static final Code DEFAULT_CODE = Util.newCode();

	//////// IfNull /////////
	public static Order getOrder(Order order) {
		return order == null ? DEFAULT_ORDER : order;
	}

	public static Customer getCustomer(Order order) {

		if (order == null) {
			return DEFAULT_CUSTOMER;
		}

		return order.getCustomerPlain() == null ? DEFAULT_CUSTOMER : order.getCustomerPlain();
	}

	public static Address getAddress1(Order order) {

		Customer customer = getCustomer(order);

		if (customer == null) {
			return DEFAULT_ADDRESS1;
		}

		return customer.getAddress1Plain() == null ? DEFAULT_ADDRESS1 : customer.getAddress1Plain();
	}

	public static Country getCountry(Order order) {

		Address address1 = getAddress1(order);

		if (address1 == null) {
			return DEFAULT_COUNTRY;
		}

		return address1.getCountryPlain() == null ? DEFAULT_COUNTRY : address1.getCountryPlain();
	}

	public static IsoCode getIsoCode(Order order) {

		Country country = getCountry(order);

		if (country == null) {
			return DEFAULT_ISO_CODE;
		}

		return country.getIsoCodePlain() == null ? DEFAULT_ISO_CODE : country.getIsoCodePlain();
	}

	public static Code getCode(Order order) {

		IsoCode isoCode = getIsoCode(order);

		if (isoCode == null) {
			return DEFAULT_CODE;
		}

		return isoCode.getCodePlain() == null ? DEFAULT_CODE : isoCode.getCodePlain();
	}

	//////// Poptional /////////
	public static Order getOrderPoptional(Order order) {
		return Poptional.ofNullable(order)
				.orElse(DEFAULT_ORDER);
	}

	public static Customer getCustomerPoptional(Order order) {
		return Poptional.ofNullable(order)
				.flatMap(Order::getCustomer)
				.orElse(DEFAULT_CUSTOMER);
	}

	public static Address getAddress1Poptional(Order order) {
		return Poptional.ofNullable(order)
				.flatMap(Order::getCustomer)
				.flatMap(Customer::getAddress1)
				.orElse(DEFAULT_ADDRESS1);
	}

	public static Country getCountryPoptional(Order order) {
		return Poptional.ofNullable(order)
				.flatMap(Order::getCustomer)
				.flatMap(Customer::getAddress1)
				.flatMap(Address::getCountry)
				.orElse(DEFAULT_COUNTRY);
	}

	public static IsoCode getIsoCodePoptional(Order order) {
		return Poptional.ofNullable(order)
				.flatMap(Order::getCustomer)
				.flatMap(Customer::getAddress1)
				.flatMap(Address::getCountry)
				.flatMap(Country::getIsoCode)
				.orElse(DEFAULT_ISO_CODE);
	}

	public static Code getCodePoptional_flatMap(Order order) {
		return Poptional.ofNullable(order)
				.flatMap(Order::getCustomer)
				.flatMap(Customer::getAddress1)
				.flatMap(Address::getCountry)
				.flatMap(Country::getIsoCode)
				.flatMap(IsoCode::getCode)
				.orElse(DEFAULT_CODE);
	}

	public static Code getCodePoptional_map(Order order) {
		return Poptional.ofNullable(order)
				.map(Order::getCustomerPlain)
				.map(Customer::getAddress1Plain)
				.map(Address::getCountryPlain)
				.map(Country::getIsoCodePlain)
				.map(IsoCode::getCodePlain)
				.orElse(DEFAULT_CODE);
	}

	//////// Optional /////////
	public static Order getOrderOptional(Order order) {
		return Optional.ofNullable(order)
				.orElse(DEFAULT_ORDER);
	}

	public static Customer getCustomerOptional(Order order) {
		return Optional.ofNullable(order)
				.map(Order::getCustomerPlain)
				.orElse(DEFAULT_CUSTOMER);
	}

	public static Address getAddress1Optional(Order order) {
		return Optional.ofNullable(order)
				.flatMap(Order::getCustomerOptional)
				.flatMap(Customer::getAddress1Optional)
				.orElse(DEFAULT_ADDRESS1);
	}

	public static Country getCountryOptional(Order order) {
		return Optional.ofNullable(order)
				.flatMap(Order::getCustomerOptional)
				.flatMap(Customer::getAddress1Optional)
				.flatMap(Address::getCountryOptional)
				.orElse(DEFAULT_COUNTRY);
	}

	public static IsoCode getIsoCodeOptional(Order order) {
		return Optional.ofNullable(order)
				.flatMap(Order::getCustomerOptional)
				.flatMap(Customer::getAddress1Optional)
				.flatMap(Address::getCountryOptional)
				.flatMap(Country::getIsoCodeOptional)
				.orElse(DEFAULT_ISO_CODE);
	}

	public static Code getCodeOptionalMap(Order order) {
		return Optional.ofNullable(order)
				.map(Order::getCustomerPlain)
				.map(Customer::getAddress1Plain)
				.map(Address::getCountryPlain)
				.map(Country::getIsoCodePlain)
				.map(IsoCode::getCodePlain)
				.orElse(DEFAULT_CODE);
	}

	public static Code getCodeOptionalFlatMap(Order order) {
		return Optional.ofNullable(order)
				.flatMap(Order::getCustomerOptional)
				.flatMap(Customer::getAddress1Optional)
				.flatMap(Address::getCountryOptional)
				.flatMap(Country::getIsoCodeOptional)
				.flatMap(IsoCode::getCodeOptional)
				.orElse(DEFAULT_CODE);
	}

	//////// Guava Optional /////////

	public static Order getOrderGuavaOptional(Order order) {
		return gopt.Goptional.fromNullable(order)
				.or(DEFAULT_ORDER);
	}

	public static Customer getCustomerGuavaOptional(Order order) {
		return gopt.Goptional.fromNullable(order)
				.transform(Order::getCustomerPlain)
				.or(DEFAULT_CUSTOMER);
	}

	public static Address getAddress1GuavaOptional(Order order) {

		return gopt.Goptional.fromNullable(order)
				.transform(Order::getCustomerPlain)
				.transform(Customer::getAddress1Plain)
				.or(DEFAULT_ADDRESS1);
	}

	public static Country getCountryGuavaOptional(Order order) {

		return gopt.Goptional.fromNullable(order)
				.transform(Order::getCustomerPlain)
				.transform(Customer::getAddress1Plain)
				.transform(Address::getCountryPlain)
				.or(DEFAULT_COUNTRY);
	}

	public static IsoCode getIsoCodeGuavaOptional(Order order) {

		return gopt.Goptional.fromNullable(order)
				.transform(Order::getCustomerPlain)
				.transform(Customer::getAddress1Plain)
				.transform(Address::getCountryPlain)
				.transform(Country::getIsoCodePlain)
				.or(DEFAULT_ISO_CODE);
	}

	public static Code getCodeGoptional(Order order) {

		return gopt.Goptional.fromNullable(order)
				.transform(Order::getCustomerPlain)
				.transform(Customer::getAddress1Plain)
				.transform(Address::getCountryPlain)
				.transform(Country::getIsoCodePlain)
				.transform(IsoCode::getCodePlain)
				.or(DEFAULT_CODE);
	}
}
