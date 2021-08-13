package poptional;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Something<T> implements Poptional<T> {

	protected Something() {
	}

	@Override
	public final boolean isEmpty() {
		return false;
	}

	@Override
	public final boolean isPresent() {
		return true;
	}

	@Override
	public final <R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse) {
		return ifPresent.apply(this.get());
	}

	@Override
	public final <R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse) {
		return ifPresent.apply(this.get());
	}

	@Override
	public final <R extends Poptional<R>> Poptional<R> flatMap(Function<? super T, ? extends Poptional<R>> mapper) {
		return mapper.apply(this.get());
	}

	@Override
	public final <U> Poptional<U> map(Function<? super T, ? extends U> mapper) {
		return Poptional.ofNullable((Poptional<U>) mapper.apply(this.get()));
	}

	@Override
	public final T orElseThrow() {
		return this.get();
	}

	@Override
	public final <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		return this.get();
	}

	@Override
	public final T orElseGet(Supplier<T> orElse) {
		return this.get();
	}

	@Override
	public final T orElse(T orElse) {
		return this.get();
	}

	@Override
	public final void ifPresent(Consumer<T> ifNotNull) {
		ifNotNull.accept(this.get());
	}

	@Override
	public final <R> R ifNullOrElse(Supplier<R> ifNull, Supplier<R> orElse) {
		return orElse.get();
	}

	@Override
	public final void ifNull(Runnable ifNull) {
		// do nothing because this is the "not null" implementation
	}

	@Override
	public final boolean isNull() {
		return false;
	}

	@Override
	public final Poptional<T> or(Supplier<? extends Poptional<? extends T>> supplier) {
		return this;
	}

	@Override
	public final T get() {
		return (T) this;
	}
}
