package com.petra.ioptional.lang;

import lombok.RequiredArgsConstructor;

/*
 * We should be able to get ride of this method once built-in data types are addressed by either:
 * 	1- Create new types that copy the exact same code from the existing types, but make it inherit from Nullable<T extends Nullable<T>>
 * 	2- Provide wrappers that delegate things
 * 	3- Leave them as is, and use the primitives which don’t allow nulls, except for String where it maybe defaulted to an empty String
 *
 */
@RequiredArgsConstructor
public final class ValueImpl<T> extends Value<T> {

	private final T value;

	@Override
	public T get() {
		return value;
	}
}
