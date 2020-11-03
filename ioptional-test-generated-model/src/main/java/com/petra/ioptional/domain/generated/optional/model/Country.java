package com.petra.ioptional.domain.generated.optional.model;

import com.petra.ioptional.lang.Empty;
import com.petra.ioptional.lang.IOptional;
import com.petra.ioptional.lang.Value;
import com.petra.ioptional.lang.EmptyImpl;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.function.Function;

@Getter
@Setter
public final class Country extends Value<Country> {

	private IsoCode isoCode;

	public IOptional<IsoCode> getIsoCode() {
		return IOptionals.ofNullable(isoCode);
	}

	public Optional<IsoCode> getOptionalIsoCode() {
		return Optional.ofNullable(isoCode);
	}

	@Override
	public Country get() {
		return this;
	}

	static final class EmptyCountry extends Empty<Country> {

		static final EmptyCountry EMPTY_COUNTRY = new EmptyCountry();

		@Override
		public <R> IOptional<R> map(Function<? super Country, R> ifPresent) {
			return EmptyImpl.EMPTY;
		}

		@Override
		public <R> IOptional<R> flatMap(Function<? super Country, ? extends IOptional<? extends R>> mapper) {
			return EmptyImpl.EMPTY;
		}
	}
}
