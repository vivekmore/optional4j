package ioptional;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static ioptional.Nothing.NOTHING;

public interface IOptional<T> {

	static <T> ioptional.IOptional<T> ofNullable(T value) {

		if (value == null) {
			return empty();
		}

		if (value instanceof ioptional.IOptional) {
			return (ioptional.IOptional) value;
		}

		return new ioptional.SomethingImpl<>(value);
	}

	static <T> ioptional.IOptional<T> ofNullable(ioptional.IOptional<T> value) {
		return value == null ? empty() : value;
	}

	static <T> ioptional.IOptional<T> empty() {
		return NOTHING;
	}

	boolean isEmpty();

	boolean isPresent();

	<R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse);

	void ifPresent(Consumer<T> ifPresent);

	<R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse);

	T orElseGet(Supplier<T> orElse);

	T orElse(T orElse);

	<R> R ifNullOrElse(Supplier<R> ifNull, Supplier<R> orElse);

	void ifNull(Runnable ifNull);

	<R> ioptional.IOptional<R> flatMap(Function<? super T, ? extends ioptional.IOptional<? extends R>> mapper);

	ioptional.IOptional<T> or(Supplier<? extends ioptional.IOptional<? extends T>> supplier);

	T orElseThrow();

	<X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

	boolean isNull();

	T get();

}
