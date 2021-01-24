/*
package com.saleh.phd;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.saleh.phd.Util.FORK_VALUE;
import static com.saleh.phd.Util.MEASUREMENT_ITERATIONS;
import static com.saleh.phd.Util.WARM_UP_ITERATIONS;

public class ListProgram {

	private static final int SIZE = 9000;

	public static void main(String[] args) throws IOException, RunnerException {
		org.openjdk.jmh.Main.main(args);
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	@Fork(FORK_VALUE)
	public static void listAccessUsingIf(Blackhole blackhole) {

		for (int i = 0; i < SIZE; i++) {

			Order anOrder = orders.get(i);

			if (anOrder == null) {
				blackhole.consume(customer);
				continue;
			}

			if (anOrder.customer == null) {
				blackhole.consume(UNNAMED; continue;
			}

			if (anOrder.customer.address == null) {
				blackhole.consume(UNNAMED);
				continue;
			}

			if (anOrder.customer.address.country == null) {
				blackhole.consume(UNNAMED);
				continue;
			}

			if (anOrder.customer.address.country.isoCode == null) {
				blackhole.consume(UNNAMED);
				continue;
			}
			if (anOrder.customer.address.country.isoCode.code == null) {
				blackhole.consume(UNNAMED);
				continue;
			}

			blackhole.consume(anOrder.customer);
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	@Fork(FORK_VALUE)
	public static void listAccessUsingNullable(Blackhole blackhole) {

		for (int i = 0; i < SIZE; i++) {

			blackhole.consume(getOrder(orders.get(i)).flatMap(Order::getCustomer)
					.flatMap(Customer::getAddress)
					.flatMap(Address::getCountry)
					.flatMap(Country::getIsoCode)
					.flatMap(IsoCode::getCode)
					.orElse(UNNAMED));
		}
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	@Fork(FORK_VALUE)
	public static void listAccessUsingOptional(Blackhole blackhole) {
		for (int i = 0; i < SIZE; i++) {
			blackhole.consume(getOrderOptional(orders.get(i)).flatMap(Order::getOptionalCustomer)
					.flatMap(Customer::getOptionalAddress)
					.flatMap(Address::getOptionalCountry)
					.flatMap(Country::getOptionalIsoCode)
					.flatMap(IsoCode::getOptionalCode)
					.orElse(UNNAMED));
		}
	}
}
*/
