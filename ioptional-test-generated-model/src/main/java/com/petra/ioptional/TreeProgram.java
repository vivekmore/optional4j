/*
package com.petra.ioptional;

import com.petra.ioptional.domain.generated.optional.model.Node;
import com.petra.ioptional.domain.generated.optional.model.Order;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.petra.ioptional.Util.FORK_VALUE;
import static com.petra.ioptional.Util.MEASUREMENT_ITERATIONS;
import static com.petra.ioptional.Util.WARM_UP_ITERATIONS;

public class TreeProgram {

	private static final String UNNAMED = "unnamed";

	private static final int MAX_TREE_HEIGHT = 20;
	private static Node node = buildTree(1);

	public static void main(String[] args) throws IOException, RunnerException {
		org.openjdk.jmh.Main.main(args);
	}

	private static Node buildTree(int height) {

		if (height < MAX_TREE_HEIGHT) {
			return newNode(buildTree(height + 1), buildTree(height + 1));
		} else {
			return leafNode();
		}
	}

	private static Node newNode(Node left, Node right) {
		return new Node(left, right, value());
	}

	private static Node leafNode() {
		return new Node(null, null, value());
	}

	private static Order value() {
		return Util.newOrder();
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	@Fork(FORK_VALUE)
	public static int getNameUsingIf() {
		return sum(node);
	}

	private static int sum(Node node) {

		int sumLeft = 0;
		int sumRight = 0;
		if (node.getLeft() != null) {
			sumLeft = sum(node.getLeft());
		}

		if (node.getRight() != null) {
			sumRight = sum(node.getRight());
		}

		Integer value = BasicProgram.getNameUsingIf(node.getOrder());

		return sumLeft + value + sumRight;
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	@Fork(FORK_VALUE)
	public static void getUsingNullable() {
		sumNullable(node);
	}

	private static int sumNullable(Node node) {

		int sumLeft = node.getLeft()
				.map(TreeProgram::sumNullable)
				.orElse(0);

		int sumRight = node.getRight()
				.map(TreeProgram::sumNullable)
				.orElse(0);

		Integer value = BasicProgram.getUsingNullable(node.getOrder());

		return sumLeft + value + sumRight;
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@Warmup(iterations = WARM_UP_ITERATIONS)
	@Measurement(iterations = MEASUREMENT_ITERATIONS)
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	@Fork(FORK_VALUE)
	public static void getUsingOptional() {
		sumOptional(node);
	}

	private static int sumOptional(Node node) {

		int sumLeft = node.getOptionalLeft()
				.map(TreeProgram::sumOptional)
				.orElse(0);

		int sumRight = node.getOptionalRight()
				.map(TreeProgram::sumOptional)
				.orElse(0);

		Integer value = BasicProgram.getUsingOptional(node.getOptionalOrder());

		return sumLeft + value + sumRight;
	}
}*/
