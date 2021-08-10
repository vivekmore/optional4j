package gopt;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

final class Present<T> extends gopt.Goptional<T> {

	private final T reference;
	private static final long serialVersionUID = 0L;

	Present(T reference) {
		this.reference = reference;
	}

	public boolean isPresent() {
		return true;
	}

	public T get() {
		return this.reference;
	}

	public T or(T defaultValue) {
		Objects.requireNonNull(defaultValue, "use gopt.Goptional.orNull() instead of gopt.Goptional.or(null)");
		return this.reference;
	}

	public gopt.Goptional<T> or(gopt.Goptional<? extends T> secondChoice) {
		Objects.requireNonNull(secondChoice);
		return this;
	}

	public T or(Supplier<? extends T> supplier) {
		Objects.requireNonNull(supplier);
		return this.reference;
	}

	public T orNull() {
		return this.reference;
	}

	public Set<T> asSet() {
		return Collections.singleton(this.reference);
	}

	// changed
	public <V> gopt.Goptional<V> transform(Function<? super T, V>  mapper) {
		return Goptional.fromNullable(mapper.apply(this.reference));
	}

	public boolean equals(Object object) {
		if (object instanceof Present) {
			Present<?> other = (Present) object;
			return this.reference.equals(other.reference);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return 1502476572 + this.reference.hashCode();
	}

	public String toString() {
		String var1 = String.valueOf(this.reference);
		return (new StringBuilder(13 + String.valueOf(var1)
				.length())).append("gopt.Goptional.of(")
				.append(var1)
				.append(")")
				.toString();
	}
}