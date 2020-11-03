/*
package com.petra.ioptional;

import com.petra.ioptional.domain.generated.optional.model.Address;
import com.petra.ioptional.domain.generated.optional.model.Country;
import com.petra.ioptional.domain.generated.optional.model.Customer;
import com.petra.ioptional.domain.generated.optional.model.IsoCode;
import com.petra.ioptional.domain.generated.optional.model.Order;
import com.petra.ioptional.lang.IOptional;

import java.util.Optional;

public class BasicProgram {

	private static final Integer UNNAMED = 0;

	//	@Benchmark
	//	@BenchmarkMode(Mode.AverageTime)
	//	@Warmup(iterations = WARM_UP_ITERATIONS)
	//	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	//	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	//	@Fork(FORK_VALUE)
	public static Integer getNameUsingIf(Order order) {

		if (order == null) {
			return UNNAMED;
		}

		if (order.getCustomer() == null) {
			return UNNAMED;
		}

		if (order.getCustomer()
				.getAddress() == null) {
			return UNNAMED;
		}

		if (order.getCustomer()
				.getAddress()
				.getCountry() == null) {
			return UNNAMED;
		}

		if (order.getCustomer()
				.getAddress()
				.getCountry()
				.getIsoCode() == null) {
			return UNNAMED;
		}

		if (order.getCustomer()
				.getAddress()
				.getCountry()
				.getIsoCode()
				.getCode() == null) {
			return UNNAMED;
		}

		return order.getCustomer()
				.getAddress()
				.getCountry()
				.getIsoCode()
				.getCode();
	}

	//	@Benchmark
	//	@BenchmarkMode(Mode.AverageTime)
	//	@Warmup(iterations = WARM_UP_ITERATIONS)
	//	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	//	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	//	@Fork(FORK_VALUE)
	public static Integer getUsingNullable(IOptional<Order> order) {

		return order.
				flatMap(Order::getCustomer)
				.flatMap(Customer::getAddress)
				.flatMap(Address::getCountry)
				.flatMap(Country::getIsoCode)
				.flatMap(IsoCode::getCode)
				.orElse(UNNAMED);

	}

	//	@Benchmark
	//	@BenchmarkMode(Mode.AverageTime)
	//	@Warmup(iterations = WARM_UP_ITERATIONS)
	//	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	//	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	//	@Fork(FORK_VALUE)
	public static Integer getUsingOptional(Optional<Order> order) {

		return order.flatMap(Order::getOptionalCustomer)
				.flatMap(Customer::getOptionalAddress)
				.flatMap(Address::getOptionalCountry)
				.flatMap(Country::getOptionalIsoCode)
				.flatMap(IsoCode::getOptionalCode)
				.orElse(UNNAMED);
	}
}
*/
