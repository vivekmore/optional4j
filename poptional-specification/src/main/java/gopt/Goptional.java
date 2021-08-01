package gopt;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Goptional<T> implements Serializable {

	private static final long serialVersionUID = 0L;

	public static <T> gopt.Goptional<T> absent() {
		return Absent.withType();
	}

	public static <T> gopt.Goptional<T> of(T reference) {
		Objects.requireNonNull(reference);
		return new Present<>(reference);
	}

	public static <T> gopt.Goptional<T> fromNullable(T nullableReference) {
		return (Goptional) (nullableReference == null ? absent() : new Present<>(nullableReference));
	}

	public static <T> gopt.Goptional<T> fromJavaUtil(java.util.Optional<T> javaUtilOptional) {
		return javaUtilOptional == null ? null : fromNullable(javaUtilOptional.orElse(null));
	}

	public static <T> java.util.Optional<T> toJavaUtil(gopt.Goptional<T> googleOptional) {
		return googleOptional == null ? null : googleOptional.toJavaUtil();
	}

	public java.util.Optional<T> toJavaUtil() {
		return java.util.Optional.ofNullable(this.orNull());
	}

	Goptional() {
	}

	public abstract boolean isPresent();

	public abstract T get();

	public abstract T or(T var1);

	public abstract gopt.Goptional<T> or(gopt.Goptional<? extends T> var1);

	public abstract T or(Supplier<? extends T> var1);

	public abstract T orNull();

	public abstract Set<T> asSet();

	public abstract <V> gopt.Goptional<V> transform(Function<? super T, V> var1);

	public abstract boolean equals(Object var1);

	public abstract int hashCode();

	public abstract String toString();
}