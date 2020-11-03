package com.petra.ioptional.domain.generated.optional.model;

import com.petra.ioptional.lang.Empty;
import com.petra.ioptional.lang.EmptyImpl;
import com.petra.ioptional.lang.IOptional;
import com.petra.ioptional.lang.Value;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Getter
@Setter
public final class Order extends Value<Order> {

	private UUID orderId;

	private Customer customer;

	public IOptional<Customer> getCustomer() {
		return IOptionals.ofNullable(customer);
	}

	public Optional<Customer> getOptionalCustomer() {
		return Optional.ofNullable(customer);
	}

	@Override
	public Order get() {
		return this;
	}

	static final class EmptyOrder extends Empty<Order> {

		static final EmptyOrder EMPTY_ORDER = new EmptyOrder();

		@Override
		public <R> IOptional<R> map(Function<? super Order, R> ifPresent) {
			return EmptyImpl.EMPTY;
		}

		@Override
		public <R> IOptional<R> flatMap(Function<? super Order, ? extends IOptional<? extends R>> mapper) {
			return EmptyImpl.EMPTY;
		}
	}
}
