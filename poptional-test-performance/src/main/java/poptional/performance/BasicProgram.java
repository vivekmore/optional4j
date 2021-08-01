package poptional.performance;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.RunnerException;
import poptional.test.model.Order;

import java.io.IOException;

import static org.openjdk.jmh.annotations.Mode.AverageTime;
import static poptional.performance.ModelUtil.getAddress1;
import static poptional.performance.ModelUtil.getAddress1GuavaOptional;
import static poptional.performance.ModelUtil.getAddress1Optional;
import static poptional.performance.ModelUtil.getAddress1Poptional;
import static poptional.performance.ModelUtil.getCode;
import static poptional.performance.ModelUtil.getCodeGoptional;
import static poptional.performance.ModelUtil.getCodeOptionalFlatMap;
import static poptional.performance.ModelUtil.getCodeOptionalMap;
import static poptional.performance.ModelUtil.getCodePoptional_flatMap;
import static poptional.performance.ModelUtil.getCodePoptional_map;
import static poptional.performance.ModelUtil.getCountry;
import static poptional.performance.ModelUtil.getCountryGuavaOptional;
import static poptional.performance.ModelUtil.getCountryOptional;
import static poptional.performance.ModelUtil.getCountryPoptional;
import static poptional.performance.ModelUtil.getCustomer;
import static poptional.performance.ModelUtil.getCustomerGuavaOptional;
import static poptional.performance.ModelUtil.getCustomerOptional;
import static poptional.performance.ModelUtil.getCustomerPoptional;
import static poptional.performance.ModelUtil.getIsoCode;
import static poptional.performance.ModelUtil.getIsoCodeGuavaOptional;
import static poptional.performance.ModelUtil.getIsoCodeOptional;
import static poptional.performance.ModelUtil.getIsoCodePoptional;
import static poptional.performance.ModelUtil.getOrder;
import static poptional.performance.ModelUtil.getOrderGuavaOptional;
import static poptional.performance.ModelUtil.getOrderOptional;
import static poptional.performance.ModelUtil.getOrderPoptional;
import static poptional.performance.Util.createOrders;

public class BasicProgram {

	private static final int SIZE = 1000;
	private static final Order[] orders = createOrders(SIZE);
	private static final Order order = orders[0];
	private static final Mode[] modes = new Mode[] { AverageTime };

	//private static final Map<Integer, Order> ordersMap = createOrdersMap(SIZE);

	public static void main(String[] args) throws IOException, RunnerException {
		org.openjdk.jmh.Main.main(args);

		//printAllMappings();
	}

	private static void printAllMappings() {
		// Order
		System.out.println(getOrder(orders[0]));
		System.out.println(getOrderPoptional(orders[0]));
		System.out.println(getOrderGuavaOptional(orders[0]));
		System.out.println(getOrderOptional(orders[0]));

		// Customer
		System.out.println(getCustomer(orders[0]));
		System.out.println(getCustomerPoptional(orders[0]));
		System.out.println(getCustomerGuavaOptional(orders[0]));
		System.out.println(getCustomerOptional(orders[0]));

		// Address
		System.out.println(getAddress1(orders[0]));
		System.out.println(getAddress1Poptional(orders[0]));
		System.out.println(getAddress1GuavaOptional(orders[0]));
		System.out.println(getAddress1Optional(orders[0]));

		// Country
		System.out.println(getCountry(orders[0]));
		System.out.println(getCountryPoptional(orders[0]));
		System.out.println(getCountryGuavaOptional(orders[0]));
		System.out.println(getCountryOptional(orders[0]));

		// Country
		System.out.println(getIsoCode(orders[0]));
		System.out.println(getIsoCodePoptional(orders[0]));
		System.out.println(getIsoCodeGuavaOptional(orders[0]));
		System.out.println(getIsoCodeOptional(orders[0]));

		// Code
		System.out.println(getCode(orders[0]));
		System.out.println(getCodePoptional_flatMap(orders[0]));
		//System.out.println(getCodePoptional_map(orders[0]));
		System.out.println(getIsoCodeGuavaOptional(orders[0]));
		System.out.println(getCodeOptionalMap(orders[0]));
		System.out.println(getCodeOptionalFlatMap(orders[0]));
	}

	@Benchmark
	public static Blackhole ifElse(Blackhole blackhole) {
		for (Order order : orders) {
			blackhole.consume(getCode(order));
		}
		return blackhole;
	}

	@Benchmark
	public static Blackhole poptional_map(Blackhole blackhole) {

		for (Order order : orders) {
			blackhole.consume(getCodePoptional_map(order));
		}
		return blackhole;
	}

	@Benchmark
	public static Blackhole poptional_flatMap(Blackhole blackhole) {

		for (Order order : orders) {
			blackhole.consume(getCodePoptional_flatMap(order));
		}
		return blackhole;
	}

	//	@Benchmark
	//	public static Blackhole optional_map(Blackhole blackhole) {
	//
	//		for (Order order : orders) {
	//			blackhole.consume(getCodeOptionalMap(order));
	//		}
	//		return blackhole;
	//	}
	//
	//	@Benchmark
	//	public static Blackhole optional_flatmap(Blackhole blackhole) {
	//
	//		for (Order order : orders) {
	//			blackhole.consume(getCodeOptionalFlatMap(order));
	//		}
	//		return blackhole;
	//	}

	@Benchmark
	public static Blackhole guava_optional(Blackhole blackhole) {

		for (Order order : orders) {
			blackhole.consume(getCodeGoptional(order));
		}
		return blackhole;
	}
}