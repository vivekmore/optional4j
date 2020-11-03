package com.petra.ioptional.lang;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IOptional<T> {

	static <T> IOptional<T> ofNullable(T value) {
		return value == null ? EmptyImpl.EMPTY : new ValueImpl<>(value);
	}

	static <T> IOptional<T> of(T value) {
		Objects.requireNonNull(value);
		return new ValueImpl<>(value);
	}

	static <T> IOptional<T> empty() {
		return EmptyImpl.EMPTY;
	}

	<R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse);

	<R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse);

	T orElseGet(Supplier<T> orElse);

	T orElse(T orElse);

	void ifPresent(Consumer<T> ifPresent);

	<R> R ifNullOrElse(Supplier<R> ifNull, Supplier<R> orElse);

	void ifNull(Runnable ifNull);

	<R> IOptional<R> map(Function<? super T, R> ifPresent);

	<R> IOptional<R> flatMap(Function<? super T, ? extends IOptional<? extends R>> mapper);

	boolean isNull();
}
