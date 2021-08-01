package poptional;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Poptional<T extends Poptional<T>> {

	static <T extends Poptional<T>> Poptional<T> ofNullable(T value) {
		return value == null ? empty() : value;
	}

	static <T extends Poptional<T>> Poptional<T> empty() {
		return Nothing.empty();
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

	<R extends Poptional<R>> Poptional<R> flatMap(Function<? super T, ? extends Poptional<R>> mapper);

	<U extends Poptional<U>> Poptional<U> map(Function<? super T, ? extends U> mapper);

	Poptional<T> or(Supplier<? extends Poptional<? extends T>> supplier);

	T orElseThrow();

	<X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

	boolean isNull();

	T get();

}
