package com.petra.ioptional;

import com.petra.ioptional.lang.IOptional;
import com.petra.ioptional.test.model.Address;
import com.petra.ioptional.test.model.Address$;
import com.petra.ioptional.test.model.Country;
import com.petra.ioptional.test.model.Country$;
import com.petra.ioptional.test.model.Customer;
import com.petra.ioptional.test.model.Customer$;
import com.petra.ioptional.test.model.IsoCode;
import com.petra.ioptional.test.model.IsoCode$;
import com.petra.ioptional.test.model.Order;
import com.petra.ioptional.test.model.Order$;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.Optional;

import static com.petra.ioptional.Util.FORK_VALUE;
import static com.petra.ioptional.Util.MEASUREMENT_ITERATIONS;
import static com.petra.ioptional.Util.WARM_UP_ITERATIONS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;

public class BasicProgram {

	static final Order order = Util.newOrder();
	static final IOptional<Order$> order$ = Util.newOrder$();
	static final Optional<Order> optionalOrder$ = Optional.ofNullable(Util.newOrder());

	private static final Integer DEFAULT_CODE = 0;
	private static final IsoCode$ ISO_CODE_$ = new IsoCode$();

	static {
		ISO_CODE_$.setCode(DEFAULT_CODE);
	}

	public static void main(String[] args) throws IOException, RunnerException {
		org.openjdk.jmh.Main.main(args);
	}

	@Benchmark
	@BenchmarkMode(AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(NANOSECONDS)
	@Fork(FORK_VALUE)
	public static Integer getNameUsingIf() {

		if (order == null) {
			return DEFAULT_CODE;
		}

		if (order.getCustomer() == null) {
			return DEFAULT_CODE;
		}

		if (order.getCustomer()
				.getAddress1() == null) {
			return DEFAULT_CODE;
		}

		if (order.getCustomer()
				.getAddress1()
				.getCountry() == null) {
			return DEFAULT_CODE;
		}

		if (order.getCustomer()
				.getAddress1()
				.getCountry()
				.getIsoCode() == null) {
			return DEFAULT_CODE;
		}

		if (order.getCustomer()
				.getAddress1()
				.getCountry()
				.getIsoCode()
				.getCode() == null) {
			return DEFAULT_CODE;
		}

		return order.getCustomer()
				.getAddress1()
				.getCountry()
				.getIsoCode()
				.getCode();
	}

	@Benchmark
	@BenchmarkMode(AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(NANOSECONDS)
	@Fork(FORK_VALUE)
	public static Integer getUsingIOptional() {

		return order$.
				flatMap(Order$::getCustomer)
				.flatMap(Customer$::getAddress1)
				.flatMap(Address$::getCountry)
				.flatMap(Country$::getIsoCode)
				.orElse(ISO_CODE_$)
				.getCode();

	}

	@Benchmark
	@BenchmarkMode(AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(NANOSECONDS)
	@Fork(FORK_VALUE)
	public static Integer getUsingOptional() {

		return optionalOrder$.map(Order::getCustomer)
				.map(Customer::getAddress1)
				.map(Address::getCountry)
				.map(Country::getIsoCode)
				.map(IsoCode::getCode)
				.orElse(DEFAULT_CODE);
	}
}