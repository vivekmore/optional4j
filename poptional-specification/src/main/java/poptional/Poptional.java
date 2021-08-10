package poptional;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Poptional<T> {

    public static <T> Poptional<T> ofNullable(Poptional<T> value) {
        return value != null ? value : Nothing.nothing();
    }

    public static <T> Poptional<T> empty(){
        return Nothing.nothing();
    }

    Poptional() {
    }

    public abstract boolean isEmpty();

    public abstract boolean isPresent();

    public abstract <R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse);

    public abstract void ifPresent(Consumer<T> ifPresent);

    public abstract <R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse);

    public abstract T orElseGet(Supplier<T> orElse);

    public abstract T orElse(T orElse);

    public abstract <R> R ifNullOrElse(Supplier<R> ifNull, Supplier<R> orElse);

    public abstract void ifNull(Runnable ifNull);

    public abstract <R extends Poptional<R>> Poptional<R> flatMap(Function<? super T, ? extends Poptional<R>> mapper);

    public abstract <U> Poptional<U> map(Function<? super T, ? extends U> mapper);

    public abstract Poptional<T> or(Supplier<? extends Poptional<? extends T>> supplier);

    public abstract T orElseThrow();

    public abstract <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    public abstract boolean isNull();

    public abstract T get();

}
