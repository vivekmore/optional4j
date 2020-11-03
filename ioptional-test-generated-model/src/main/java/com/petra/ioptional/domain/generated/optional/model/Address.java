package com.petra.ioptional.domain.generated.optional.model;

import com.petra.ioptional.lang.Empty;
import com.petra.ioptional.lang.IOptional;
import com.petra.ioptional.lang.EmptyImpl;
import com.petra.ioptional.lang.Value;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.function.Function;

@Getter
@Setter
public final class Address extends Value<Address> {

	private Country country;

	private String street;

	private String city;

	private String zipcode;

	public IOptional<Country> getCountry() {
		return IOptionals.ofNullable(country);
	}

	public Optional<Country> getOptionalCountry() {
		return Optional.ofNullable(country);
	}

	public IOptional<String> getStreet() {
		return IOptionals.ofNullable(street);
	}

	public Optional<String> getOptionalStreet() {
		return Optional.ofNullable(street);
	}

	@Override
	public Address get() {
		return this;
	}

	static final class EmptyAddress extends Empty<Address> {

		static final EmptyAddress EMPTY_ADDRESS = new EmptyAddress();

		@Override
		public <R> IOptional<R> map(Function<? super Address, R> ifPresent) {
			return EmptyImpl.EMPTY;
		}

		@Override
		public <R> IOptional<R> flatMap(Function<? super Address, ? extends IOptional<? extends R>> mapper) {
			return EmptyImpl.EMPTY;
		}
	}
}
