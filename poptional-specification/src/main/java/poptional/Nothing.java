package poptional;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Nothing<T> extends Poptional<T> {

	public static final Nothing NOTHING = new Nothing();

	protected Nothing() {
	}

	static <T> Nothing<T> nothing(){
		return NOTHING;
	}

	@Override
	public final boolean isEmpty() {
		return true;
	}

	@Override
	public final boolean isPresent() {
		return false;
	}

	@Override
	public final <R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse) {
		return ifPresentOrElse(ifPresent, orElse.get());
	}

	@Override
	public final <R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse) {
		return orElse;
	}

	@Override
	public final T orElseGet(Supplier<T> orElse) {
		return orElse.get();
	}

	@Override
	public final T orElse(T orElse) {
		return orElse;
	}

	@Override
	public final void ifPresent(Consumer<T> ifNotNull) {
		// do nothing because this is the "null" implementation
	}

	@Override
	public final <R> R ifNullOrElse(Supplier<R> ifNull, Supplier<R> orElse) {
		return ifNull.get();
	}

	@Override
	public final void ifNull(Runnable ifNull) {
		ifNull.run();
	}

	@Override
	public final boolean isNull() {
		return true;
	}

	@Override
	public final T orElseThrow() {
		throw new NoSuchElementException("No value present");
	}

	@Override
	public final <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		throw exceptionSupplier.get();
	}

	@Override
	public final <R extends Poptional<R>> Poptional<R> flatMap(Function<? super T, ? extends Poptional<R>> mapper) {
		return (Poptional<R>) this;
	}

	@Override
	public final <U> Poptional<U> map(Function<? super T, ? extends U> mapper) {
		return (Poptional<U>) this;
	}

	@Override
	public final Poptional<T> or(Supplier<? extends Poptional<? extends T>> supplier) {
		return (Poptional) supplier.get();
	}

	@Override
	public final T get(){
		throw new NoSuchElementException("No value present");
	}

	public final boolean equals(Object object) {
		return object == this;
	}

	public final int hashCode() {
		return 704073337;
	}

	public final String toString() {
		return "poptional.Poptional.empty()";
	}
}
