package poptional.performance;

import poptional.Poptional;
import poptional.test.model.Address;
import poptional.test.model.Country;
import poptional.test.model.Customer;
import poptional.test.model.IsoCode;
import poptional.test.model.Order;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.Optional;

import static poptional.performance.Util.FORK_VALUE;
import static poptional.performance.Util.MEASUREMENT_ITERATIONS;
import static poptional.performance.Util.WARM_UP_ITERATIONS;
import static poptional.performance.Util.newOrder;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;

public class BasicProgram {

	//static final Order order = newOrder();
	static final Poptional<Order> poptionalOrder = newOrder();
	static final Optional<Order> optionalOrder = Optional.ofNullable(newOrder());

	private static final Integer DEFAULT_CODE = 0;
	private static final IsoCode ISO_CODE_ = new IsoCode();

	static {
		ISO_CODE_.setCode(DEFAULT_CODE);
	}

	public static void main(String[] args) throws IOException, RunnerException {
		org.openjdk.jmh.Main.main(args);
	}

//	@Benchmark
//	@BenchmarkMode(AverageTime)
//	@Warmup(iterations = WARM_UP_ITERATIONS)
//	@Measurement(iterations = MEASUREMENT_ITERATIONS)
//	@OutputTimeUnit(NANOSECONDS)
//	@Fork(FORK_VALUE)
//	public static Integer getNameUsingIf() {
//
//		if (order == null) {
//			return DEFAULT_CODE;
//		}
//
//		if (order.customer == null) {
//			return DEFAULT_CODE;
//		}
//
//		if (order.customer.address1 == null) {
//			return DEFAULT_CODE;
//		}
//
//		if (order.customer.address1.country == null) {
//			return DEFAULT_CODE;
//		}
//
//		if (order.customer.address1.country.isoCode == null) {
//			return DEFAULT_CODE;
//		}
//
//		if (order.customer.address1.country.isoCode.code == null) {
//			return DEFAULT_CODE;
//		}
//
//		return order.customer.address1.country.isoCode.code;
//	}

	@Benchmark
	@BenchmarkMode(AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(NANOSECONDS)
	@Fork(FORK_VALUE)
	public static Integer getUsingIOptional() {

		return poptionalOrder.flatMap(Order::getCustomer)
				.flatMap(Customer::getAddress1)
				.flatMap(Address::getCountry)
				.flatMap(Country::getIsoCode)
				.orElse(ISO_CODE_)
				.getCode();

	}

	@Benchmark
	@BenchmarkMode(AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(NANOSECONDS)
	@Fork(FORK_VALUE)
	public static Integer getUsingOptionalFlatMapVariation() {

		return optionalOrder.flatMap(Order::getCustomerOptional)
				.flatMap(Customer::getAddress1Optional)
				.flatMap(Address::getCountryOptional)
				.flatMap(Country::getIsoCodeOptional)
				.orElse(ISO_CODE_)
				.getCode();
	}

	@Benchmark
	@BenchmarkMode(AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(NANOSECONDS)
	@Fork(FORK_VALUE)
	public static Integer getUsingOptionalMapVariation() {

		return optionalOrder.map(Order::getCustomerPlain)
				.map(Customer::getAddress1Plain)
				.map(Address::getCountryPlain)
				.map(Country::getIsoCodePlain)
				.map(IsoCode::getCode)
				.orElse(DEFAULT_CODE);
	}
}