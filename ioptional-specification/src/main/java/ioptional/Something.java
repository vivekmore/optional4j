package ioptional;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Something<T> extends IOptional<T> {

	@Override
	default boolean isEmpty() {
		return false;
	}

	@Override
	default boolean isPresent() {
		return true;
	}

	@Override
	default <R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse) {
		return ifPresent.apply(get());
	}

	@Override
	default <R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse) {
		return ifPresent.apply(get());
	}

	@Override
	default <R> ioptional.IOptional<R> flatMap(Function<? super T, ? extends ioptional.IOptional<? extends R>> mapper) {
		return (ioptional.IOptional) mapper.apply(get());
	}

	@Override
	default T orElseThrow() {
		return (T) this;
	}

	@Override
	default <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		return (T) this;
	}

	@Override
	default T orElseGet(Supplier<T> orElse) {
		return get();
	}

	@Override
	default T orElse(T orElse) {
		return get();
	}

	@Override
	default void ifPresent(Consumer<T> ifNotNull) {
		ifNotNull.accept(get());
	}

	@Override
	default <R> R ifNullOrElse(Supplier<R> ifNull, Supplier<R> orElse) {
		return orElse.get();
	}

	@Override
	default void ifNull(Runnable ifNull) {
		// do nothing because this is the "not null" implementation
	}

	@Override
	default boolean isNull() {
		return false;
	}

	@Override
	default ioptional.IOptional<T> or(Supplier<? extends ioptional.IOptional<? extends T>> supplier) {
		return this;
	}

	@Override
	default T get() {
		return (T) this;
	}

}
