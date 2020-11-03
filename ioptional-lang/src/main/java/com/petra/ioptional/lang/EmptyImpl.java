package com.petra.ioptional.lang;

import java.util.function.Function;

public final class EmptyImpl<T> extends Empty<T> {

	public static final EmptyImpl EMPTY = new EmptyImpl();

	@Override
	public <R> IOptional<R> map(Function<? super T, R> ifPresent) {
		return EMPTY;
	}

	@Override
	public <R> IOptional<R> flatMap(Function<? super T, ? extends IOptional<? extends R>> mapper) {
		return EMPTY;
	}
}
