package optional4j.spec;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.NonNull;
import javax.annotation.Nullable;

public interface Optional<T> {

    @NonNull
    static <T> Optional<T> ofNullable(@Nullable T value) {
        return value != null ? Present.of(value) : Absent.nothing();
    }

    @NonNull
    static <T> Optional<T> ofNullable(@Nullable Optional<T> value) {
        return value != null ? value : Absent.nothing();
    }

    @NonNull
    static <T> Optional<T> of(@NonNull T value) {
        return Present.of(value);
    }

    @NonNull
    static <T> Optional<T> empty() {
        return Absent.nothing();
    }

    @NonNull
    java.util.Optional<T> toJavaUtil();

    boolean isEmpty();

    boolean isPresent();

    <R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse);

    <R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse);

    void ifPresent(Consumer<T> ifPresent);

    T orElseGet(Supplier<T> orElse);

    T orElse(T orElse);

    <R> R ifNullOrElse(Supplier<R> ifNull, Supplier<R> orElse);

    void ifNull(Runnable ifNull);

    T orNull();

    @NonNull
    <R> Optional<R> flatMap(Function<? super T, Optional<R>> mapper);

    <R extends NullableObject> R map(Function<? super T, R> mapper, Supplier<R> ifEmpty);

    @NonNull
    <U> Optional<U> map(Function<? super T, ? extends U> mapper);

    @NonNull
    Optional<T> or(Supplier<? extends Optional<? extends T>> supplier);

    @NonNull
    T orElseThrow();

    @NonNull
    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    boolean isNull();

    T get();
}
