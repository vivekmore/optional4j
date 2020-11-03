package com.petra.ioptional.domain.generated.optional.model;

import com.petra.ioptional.lang.Empty;
import com.petra.ioptional.lang.IOptional;
import com.petra.ioptional.lang.EmptyImpl;
import com.petra.ioptional.lang.Value;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public final class Customer extends Value<Customer> {

	private String firstName;

	private String lastName;

	private String position;

	private Address address;

	public IOptional<Address> getAddress() {
		return IOptionals.ofNullable(address);
	}

	public Optional<Address> getOptionalAddress() {
		return Optional.ofNullable(address);
	}

	@Override
	public final Customer get() {
		return this;
	}

	static final class EmptyCustomer extends Empty<Customer> {

		static final EmptyCustomer EMPTY_CUSTOMER = new EmptyCustomer();

		@Override
		public <R> IOptional<R> map(Function<? super Customer, R> ifPresent) {
			return EmptyImpl.EMPTY;
		}

		@Override
		public <R> IOptional<R> flatMap(Function<? super Customer, ? extends IOptional<? extends R>> mapper) {
			return EmptyImpl.EMPTY;
		}
	}
}