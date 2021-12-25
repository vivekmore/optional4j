package optional4j.spec;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.NonNull;

public interface Present<T> extends Optional<T> {

    @NonNull
    static <T extends Present<? extends T>> T of(T value) {
        Objects.requireNonNull(value, "`value` MUST NOT be null");
        return value;
    }

    @NonNull
    static <T> Present<T> of(T value) {
        return Value.of(value);
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    @Override
    default boolean isPresent() {
        return true;
    }

    @Override
    default <R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse) {
        return ifPresent.apply(this.get());
    }

    @Override
    default <R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse) {
        return ifPresent.apply(this.get());
    }

    @Override
    default <R extends Optional<R>> Optional<R> flatMap(
            Function<? super T, ? extends Optional<R>> mapper) {
        return mapper.apply(this.get());
    }

    @Override
    default <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        return Optional.ofNullable(mapper.apply(this.get()));
    }

    @Override
    default java.util.Optional toJavaUtil() {
        return java.util.Optional.of(get());
    }

    @Override
    default T orElseThrow() {
        return this.get();
    }

    @Override
    default <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return this.get();
    }

    @Override
    default T orElseGet(Supplier<T> orElse) {
        return this.get();
    }

    @Override
    default <R extends NullableObject> R map(Function<? super T, R> mapper, Supplier<R> ifEmpty) {
        return mapper.apply(get());
    }

    @Override
    default T orElse(T orElse) {
        return this.get();
    }

    @Override
    default void ifPresent(Consumer<T> ifNotNull) {
        ifNotNull.accept(this.get());
    }

    @Override
    default <R> R ifNullOrElse(Supplier<R> ifNull, Supplier<R> orElse) {
        return orElse.get();
    }

    @Override
    default void ifNull(Runnable ifNull) {
        // do nothing because this is the "not null" implementation
    }

    @Override
    default T orNull() {
        return get();
    }

    @Override
    default boolean isNull() {
        return false;
    }

    @Override
    default Optional<T> or(Supplier<? extends Optional<? extends T>> supplier) {
        return this;
    }

    @Override
    default T get() {
        return (T) this;
    }

    final class Value<T> implements Present<T> {

        @NonNull private final T value;

        static <T> Present<T> of(T value) {
            Objects.requireNonNull(value, "`value` MUST NOT be null");
            return new Value<>(value);
        }

        Value(@NonNull T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }
    }
}
