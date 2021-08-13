package poptional;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Poptional<T> {

    static <T> Poptional<T> ofNullable(Poptional<T> value) {
        return value != null ? value : Nothing.nothing();
    }

    static <T> Poptional<T> empty() {
        return Nothing.nothing();
    }

    boolean isEmpty();

    boolean isPresent();

    <R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse);

    void ifPresent(Consumer<T> ifPresent);

    <R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse);

    T orElseGet(Supplier<T> orElse);

    T orElse(T orElse);

    <R> R ifNullOrElse(Supplier<R> ifNull, Supplier<R> orElse);

    void ifNull(Runnable ifNull);

    <R extends Poptional<R>> Poptional<R> flatMap(Function<? super T, ? extends Poptional<R>> mapper);

    <U> Poptional<U> map(Function<? super T, ? extends U> mapper);

    Poptional<T> or(Supplier<? extends Poptional<? extends T>> supplier);

    T orElseThrow();

    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    boolean isNull();

    T get();

}
