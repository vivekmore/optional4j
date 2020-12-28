package com.petra.ioptional.domain.generated.optional.model;

import com.petra.ioptional.lang.Empty;
import com.petra.ioptional.lang.IOptional;
import com.petra.ioptional.test.model.Address$;
import com.petra.ioptional.test.model.Country$;
import com.petra.ioptional.test.model.Customer$;
import com.petra.ioptional.test.model.IsoCode$;
import com.petra.ioptional.test.model.Node$;
import com.petra.ioptional.test.model.Order$;

import java.util.Objects;

public final class IOptionals {

	private IOptionals() {
	}

	public static <T> IOptional<T> ofNullable(T value) {
		return IOptional.ofNullable(value);
	}

	public static <T> IOptional<T> of(T value) {
		return IOptional.of(value);
	}

	public static IOptional<Customer$> ofNullable(Customer$ customer$) {
		return customer$ == null ? emptyCustomer$() : customer$;
	}

	public static IOptional<Customer$> emptyCustomer$() {
		return Empty.EMPTY;
	}

	public static Customer$ of(Customer$ customer$) {
		return Objects.requireNonNull(customer$);
	}

	public static IOptional<Address$> ofNullable(Address$ address$) {
		return address$ == null ? emptyAddress$() : address$;
	}

	public static IOptional<Address$> emptyAddress$() {
		return Empty.EMPTY;
	}

	public static Address$ of(Address$ address$) {
		return Objects.requireNonNull(address$);
	}

	public static IOptional<Country$> ofNullable(Country$ country$) {
		return country$ == null ? emptyCountry$() : country$;
	}

	public static IOptional<Country$> emptyCountry$() {
		return Empty.EMPTY;
	}

	public static Country$ of(Country$ country$) {
		return Objects.requireNonNull(country$);
	}

	public static IOptional<IsoCode$> ofNullable(IsoCode$ isoCode$) {
		return isoCode$ == null ? emptyIsoCode$() : isoCode$;
	}

	private static IOptional<IsoCode$> emptyIsoCode$() {
		return Empty.EMPTY;
	}

	public static IsoCode$ of(IsoCode$ isoCode$) {
		return Objects.requireNonNull(isoCode$);
	}

	public static IOptional<Node$> ofNullable(Node$ node$) {
		return node$ == null ? emptyNode$() : node$;
	}

	public static Node$ of(Node$ node$) {
		return Objects.requireNonNull(node$);
	}

	public static IOptional<Node$> emptyNode$() {
		return Empty.EMPTY;
	}

	public static IOptional<Order$> ofNullable(Order$ order$) {
		return order$ == null ? emptyOrder$() : order$;
	}

	public static IOptional<Order$> emptyOrder$() {
		return Empty.EMPTY;
	}

	public static Order$ of(Order$ order$) {
		return Objects.requireNonNull(order$);
	}
}
