package com.petra.ioptional;

import com.petra.ioptional.lang.IOptional;
import com.petra.ioptional.test.model.Address$;
import com.petra.ioptional.test.model.Country$;
import com.petra.ioptional.test.model.Customer$;
import com.petra.ioptional.test.model.IsoCode$;
import com.petra.ioptional.test.model.Order$;

import java.util.Optional;

public class BasicProgram {

	private static final Integer UNNAMED = 0;
	private static final IsoCode$ ISO_CODE_$ = new IsoCode$();

	static {
		ISO_CODE_$.setCode(UNNAMED);
	}

	//	@Benchmark
	//	@BenchmarkMode(Mode.AverageTime)
	//	@Warmup(iterations = WARM_UP_ITERATIONS)
	//	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	//	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	//	@Fork(FORK_VALUE)
	public static Integer getNameUsingIf(Order$ order$) {

		if (order$ == null) {
			return UNNAMED;
		}

		if (order$.getCustomer() == null) {
			return UNNAMED;
		}

		if (order$.getCustomerPlain()
				.getAddress1Plain() == null) {
			return UNNAMED;
		}

		if (order$.getCustomerPlain()
				.getAddress1Plain()
				.getCountry() == null) {
			return UNNAMED;
		}

		if (order$.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain()
				.getIsoCodePlain() == null) {
			return UNNAMED;
		}

		if (order$.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain()
				.getIsoCodePlain()
				.getCode() == null) {
			return UNNAMED;
		}

		return order$.getCustomerPlain()
				.getAddress1Plain()
				.getCountryPlain()
				.getIsoCodePlain()
				.getCode();
	}

	//	@Benchmark
	//	@BenchmarkMode(Mode.AverageTime)
	//	@Warmup(iterations = WARM_UP_ITERATIONS)
	//	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	//	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	//	@Fork(FORK_VALUE)
	public static Integer getUsingNullable(IOptional<Order$> order$) {

		return order$.
				flatMap(Order$::getCustomer)
				.flatMap(Customer$::getAddress1)
				.flatMap(Address$::getCountry)
				.flatMap(Country$::getIsoCode)
				.orElse(ISO_CODE_$)
				.getCode();

	}

	//	@Benchmark
	//	@BenchmarkMode(Mode.AverageTime)
	//	@Warmup(iterations = WARM_UP_ITERATIONS)
	//	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	//	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	//	@Fork(FORK_VALUE)
	public static Integer getUsingOptional(Optional<Order$> order$) {

		return order$.flatMap(Order$::getCustomerOptional)
				.flatMap(Customer$::getAddress1Optional)
				.flatMap(Address$::getCountryOptional)
				.flatMap(Country$::getIsoCodeOptional)
				.orElse(ISO_CODE_$)
				.getCode();
	}
}
