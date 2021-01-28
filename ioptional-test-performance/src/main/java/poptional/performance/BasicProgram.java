package poptional.performance;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;
import poptional.test.model.Order;

import java.io.IOException;
import java.util.Map;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static poptional.performance.ModelUtil.getCountry;
import static poptional.performance.ModelUtil.getCountryOptional;
import static poptional.performance.ModelUtil.getCountryPoptional;
import static poptional.performance.Util.FORK_VALUE;
import static poptional.performance.Util.MEASUREMENT_ITERATIONS;
import static poptional.performance.Util.WARM_UP_ITERATIONS;
import static poptional.performance.Util.createOrders;
import static poptional.performance.Util.createOrdersMap;

public class BasicProgram {

	private static final int SIZE = 10_000;
	private static final Order[] orders = createOrders(SIZE);
	private static final Map<Integer, Order> ordersMap = createOrdersMap(SIZE);

	public static void main(String[] args) throws IOException, RunnerException {
		org.openjdk.jmh.Main.main(args);
	}

	@Benchmark
	@BenchmarkMode(AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(NANOSECONDS)
	@Fork(FORK_VALUE)
	public static void ifNull(Blackhole blackhole) {

		for (Order order : orders) {
			blackhole.consume(getCountry(order));
		}
	}

	@Benchmark
	@BenchmarkMode(AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(NANOSECONDS)
	@Fork(FORK_VALUE)
	public static void poptional(Blackhole blackhole) {

		for (Order order : orders) {
			blackhole.consume(getCountryPoptional(order));
		}
	}

	@Benchmark
	@BenchmarkMode(AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(NANOSECONDS)
	@Fork(FORK_VALUE)
	public static void op_flatmap(Blackhole blackhole) {

		for (Order order : orders) {
			blackhole.consume(getCountryOptional(order));
		}
	}
}