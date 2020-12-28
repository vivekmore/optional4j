package com.petra.ioptional.lang;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Value<T> implements IOptional<T> {

	@Override
	public final <R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse) {
		return ifPresent.apply(get());
	}

	@Override
	public <R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse) {
		return ifPresent.apply(get());
	}

	@Override
	public <R> IOptional<R> flatMap(Function<? super T, ? extends IOptional<? extends R>> mapper) {
		return (IOptional<R>) mapper.apply(get());
	}

	@Override
	public T orElseGet(Supplier<T> orElse) {
		return get();
	}

	@Override
	public T orElse(T orElse) {
		return get();
	}

	@Override
	public <R> IOptional<R> map(Function<? super T, R> map) {
		return IOptional.ofNullable(map.apply(get()));
	}

	public final void ifPresent(Consumer<T> ifNotNull) {
		ifNotNull.accept(get());
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

	public T get() {
		return (T) this;
	}
}
