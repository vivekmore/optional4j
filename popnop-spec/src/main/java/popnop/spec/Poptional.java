package popnop.spec;

import javax.annotation.NonNull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface  Poptional<T> {

    @NonNull
    static <T> Poptional<T> ofNullable(@Nullable T value) {
        return value != null ? Something.of(value) : Nothing.nothing();
    }

    @NonNull
    static <T> Poptional<T> of(@NonNull  T value) {
        return value != null ? Something.of(value) : Nothing.nothing();
    }

    @NonNull
    static <T> Poptional<T> ofNullable(@Nullable Poptional<T> value) {
        return value != null ? value : Nothing.nothing();
    }

    @NonNull
    static <T> Poptional<T> empty() {
        return Nothing.nothing();
    }

    Optional<T> toJavaUtil();

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

    <R extends NullObject> R map(Function<? super T, R> mapper, Supplier<R> ifEmpty);

    <U> Poptional<U> map(Function<? super T, ? extends U> mapper);

    Poptional<T> or(Supplier<? extends Poptional<? extends T>> supplier);

    T orElseThrow();

    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    boolean isNull();

    T get();

}
