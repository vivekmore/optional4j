package com.petra.ioptional.lang;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Empty<T> implements IOptional<T> {

	public static final Empty EMPTY = new Empty<>();

	@Override
	public final <R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse) {
		return ifPresentOrElse(ifPresent, orElse.get());
	}

	@Override
	public <R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse) {
		return orElse;
	}

	@Override
	public T orElseGet(Supplier<T> orElse) {
		return orElse.get();
	}

	@Override
	public T orElse(T orElse) {
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
	public <R> IOptional<R> map(Function<? super T, R> ifPresent) {
		return Empty.EMPTY;
	}

	@Override
	public <R> IOptional<R> flatMap(Function<? super T, ? extends IOptional<? extends R>> mapper) {
		return Empty.EMPTY;
	}
}
