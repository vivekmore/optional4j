package com.petra.ioptional.domain.generated.optional.model;

import com.petra.ioptional.lang.IOptional;

import java.util.Objects;

import static com.petra.ioptional.domain.generated.optional.model.IsoCode.EmptyIsoCode.EMPTY_IsoCode;
import static com.petra.ioptional.domain.generated.optional.model.Node.EmptyNode.EMPTY_NODE;
import static com.petra.ioptional.domain.generated.optional.model.Order.EmptyOrder.EMPTY_ORDER;

public final class IOptionals {

	private IOptionals() {
	}

	public static <T> IOptional<T> ofNullable(T value) {
		return IOptional.ofNullable(value);
	}

	public static <T> IOptional<T> of(T value) {
		return IOptional.of(value);
	}

	public static IOptional<Customer> ofNullable(Customer customer) {
		return customer == null ? emptyCustomer() : customer;
	}

	public static IOptional<Customer> emptyCustomer() {
		return Customer.EmptyCustomer.EMPTY_CUSTOMER;
	}

	public static Customer of(Customer customer) {
		return Objects.requireNonNull(customer);
	}

	public static IOptional<Address> ofNullable(Address address) {
		return address == null ? emptyAddress() : address;
	}

	public static IOptional<Address> emptyAddress() {
		return Address.EmptyAddress.EMPTY_ADDRESS;
	}

	public static Address of(Address address) {
		return Objects.requireNonNull(address);
	}

	public static IOptional<Country> ofNullable(Country country) {
		return country == null ? emptyCountry() : country;
	}

	public static IOptional<Country> emptyCountry() {
		return Country.EmptyCountry.EMPTY_COUNTRY;
	}

	public static Country of(Country country) {
		return Objects.requireNonNull(country);
	}

	public static IOptional<IsoCode> ofNullable(IsoCode isoCode) {
		return isoCode == null ? emptyIsoCode() : isoCode;
	}

	private static IOptional<IsoCode> emptyIsoCode() {
		return EMPTY_IsoCode;
	}

	public static IsoCode of(IsoCode isoCode) {
		return Objects.requireNonNull(isoCode);
	}

	public static IOptional<Node> ofNullable(Node node) {
		return node == null ? emptyNode() : node;
	}

	public static Node of(Node node) {
		return Objects.requireNonNull(node);
	}

	public static IOptional<Node> emptyNode() {
		return EMPTY_NODE;
	}

	public static IOptional<Order> ofNullable(Order order) {
		return order == null ? emptyOrder() : order;
	}

	public static IOptional<Order> emptyOrder() {
		return EMPTY_ORDER;
	}

	public static Order of(Order order) {
		return Objects.requireNonNull(order);
	}
}
