package com.petra.ioptional.domain.generated.optional.model;

import com.petra.ioptional.lang.Empty;
import com.petra.ioptional.lang.IOptional;
import com.petra.ioptional.lang.EmptyImpl;
import com.petra.ioptional.lang.Value;
import lombok.EqualsAndHashCode;

import java.util.Optional;
import java.util.function.Function;

@lombok.Value
@EqualsAndHashCode(callSuper = false)
public final class Node extends Value<Node> {

	private final Node left;

	private final Node right;

	private final Order order;

	@Override
	public Node get() {
		return this;
	}

	public IOptional<Order> getOrder() {
		return IOptionals.ofNullable(order);
	}

	public IOptional<Node> getLeft() {
		return IOptionals.ofNullable(left);
	}

	public IOptional<Node> getRight() {
		return IOptionals.ofNullable(right);
	}

	public Optional<Order> getOptionalOrder() {
		return Optional.ofNullable(order);
	}

	public Optional<Node> getOptionalLeft() {
		return Optional.ofNullable(left);
	}

	public Optional<Node> getOptionalRight() {
		return Optional.ofNullable(right);
	}

	static final class EmptyNode extends Empty<Node> {

		static final EmptyNode EMPTY_NODE = new EmptyNode();

		@Override
		public <R> IOptional<R> map(Function<? super Node, R> ifPresent) {
			return EmptyImpl.EMPTY;
		}

		@Override
		public <R> IOptional<R> flatMap(Function<? super Node, ? extends IOptional<? extends R>> mapper) {
			return EmptyImpl.EMPTY;
		}
	}
}
