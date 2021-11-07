package gopt;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

final class Absent<T> extends Goptional<T> {

    static final Absent INSTANCE = new Absent();

    private static final long serialVersionUID = 0L;

    static <T> Goptional<T> withType() {
        return INSTANCE;
    }

    private Absent() {}

    public boolean isPresent() {
        return false;
    }

    public T get() {
        throw new IllegalStateException("gopt.Goptional.get() cannot be called on an absent value");
    }

    public T or(T defaultValue) {
        return Objects.requireNonNull(
                defaultValue, "use gopt.Goptional.orNull() instead of gopt.Goptional.or(null)");
    }

    public Goptional<T> or(gopt.Goptional<? extends T> secondChoice) {
        return (Goptional) Objects.requireNonNull(secondChoice);
    }

    public T or(Supplier<? extends T> supplier) {
        return Objects.requireNonNull(
                supplier.get(),
                "use gopt.Goptional.orNull() instead of a Supplier that returns null");
    }

    public T orNull() {
        return null;
    }

    public Set<T> asSet() {
        return Collections.emptySet();
    }

    public <V> gopt.Goptional<V> transform(Function<? super T, V> function) {
        return gopt.Goptional.absent();
    }

    public boolean equals(Object object) {
        return object == this;
    }

    public int hashCode() {
        return 2040732332;
    }

    public String toString() {
        return "gopt.Goptional.absent()";
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
