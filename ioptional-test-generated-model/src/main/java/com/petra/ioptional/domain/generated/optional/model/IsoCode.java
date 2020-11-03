package com.petra.ioptional.domain.generated.optional.model;

import com.petra.ioptional.lang.Empty;
import com.petra.ioptional.lang.IOptional;
import com.petra.ioptional.lang.EmptyImpl;
import com.petra.ioptional.lang.Value;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.function.Function;

@Getter
@Setter
public final class IsoCode extends Value<IsoCode> {

	private Integer code;

	public IOptional<Integer> getCode() {
		return IOptionals.ofNullable(code);
	}

	public Optional<Integer> getOptionalCode() {
		return Optional.ofNullable(code);
	}

	@Override
	public IsoCode get() {
		return this;
	}

	static final class EmptyIsoCode extends Empty<IsoCode> {

		static final EmptyIsoCode EMPTY_IsoCode = new EmptyIsoCode();

		@Override
		public <R> IOptional<R> map(Function<? super IsoCode, R> ifPresent) {
			return EmptyImpl.EMPTY;
		}

		@Override
		public <R> IOptional<R> flatMap(Function<? super IsoCode, ? extends IOptional<? extends R>> mapper) {
			return EmptyImpl.EMPTY;
		}
	}
}
