package poptional;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Nothing<T> extends Poptional<T> {

	poptional.Nothing NOTHING = new poptional.NothingImpl();

	static <T> poptional.Nothing<T> empty(){
		return NOTHING;
	}

	@Override
	default boolean isEmpty() {
		return true;
	}

	@Override
	default boolean isPresent() {
		return false;
	}

	@Override
	default <R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse) {
		return ifPresentOrElse(ifPresent, orElse.get());
	}

	@Override
	default <R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse) {
		return orElse;
	}

	@Override
	default T orElseGet(Supplier<T> orElse) {
		return orElse.get();
	}

	@Override
	default T orElse(T orElse) {
		return orElse;
	}

	@Override
	default void ifPresent(Consumer<T> ifNotNull) {
		// do nothing because this is the "null" implementation
	}

	@Override
	default <R> R ifNullOrElse(Supplier<R> ifNull, Supplier<R> orElse) {
		return ifNull.get();
	}

	@Override
	default void ifNull(Runnable ifNull) {
		ifNull.run();
	}

	@Override
	default boolean isNull() {
		return true;
	}

	@Override
	default T orElseThrow() {
		throw new NoSuchElementException("No value present");
	}

	@Override
	default <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		throw exceptionSupplier.get();
	}

	@Override
	default <R> Poptional<R> flatMap(Function<? super T, ? extends Poptional<? extends R>> mapper) {
		return NOTHING;
	}

	@Override
	default Poptional<T> or(Supplier<? extends Poptional<? extends T>> supplier) {
		return (Poptional) supplier.get();
	}

	@Override
	default T get(){
		throw new NoSuchElementException("No value present");
	}

}
