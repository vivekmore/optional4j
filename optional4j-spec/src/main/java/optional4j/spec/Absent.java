package optional4j.spec;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Absent<T> extends Optional<T> {

    Absent NULL = new Null();

    static <T> Absent<T> nothing() {
        return NULL;
    }

    @Override
    default boolean isEmpty() {
        return true;
    }

    @Override
    default boolean isPresent() {
        return false;
    }

    @Override
    default <R> R ifPresentOrElseGet(Function<? super T, R> ifPresent, Supplier<R> orElse) {
        return ifPresentOrElse(ifPresent, orElse.get());
    }

    @Override
    default <R> R ifPresentOrElse(Function<? super T, R> ifPresent, R orElse) {
        return orElse;
    }

    @Override
    default T orElseGet(Supplier<T> orElse) {
        return orElse.get();
    }

    @Override
    default java.util.Optional<T> toJavaUtil() {
        return java.util.Optional.empty();
    }

    @Override
    default T orElse(T orElse) {
        return orElse;
    }

    @Override
    default void ifPresent(Consumer<T> ifNotNull) {
        // do nothing because this is the "null" implementation
    }

    @Override
    default <R> R ifNullOrElse(Supplier<R> ifNull, Supplier<R> orElse) {
        return ifNull.get();
    }

    @Override
    default void ifNull(Runnable ifNull) {
        ifNull.run();
    }

    @Override
    default T orNull() {
        return null;
    }

    @Override
    default boolean isNull() {
        return true;
    }

    @Override
    default T orElseThrow() {
        throw new NoSuchElementException("No value present");
    }

    @Override
    default <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        throw exceptionSupplier.get();
    }

    @Override
    default <R> Optional<R> flatMap(Function<? super T, Optional<R>> mapper) {
        return (Optional<R>) this;
    }

    @Override
    default <R extends NullableObject> R map(Function<? super T, R> mapper, Supplier<R> ifEmpty) {
        return ifEmpty.get();
    }

    @Override
    default <U> Optional<U> map(Function<? super T, ? extends U> mapper) {
        return (Optional<U>) this;
    }

    @Override
    default Optional<T> or(Supplier<? extends Optional<? extends T>> supplier) {
        return (Optional) supplier.get();
    }

    @Override
    default T get() {
        throw new NoSuchElementException("No value present");
    }

    final class Null<T> implements Absent<T> {

        Null() {}

        public boolean equals(Object object) {
            return object == this;
        }

        public int hashCode() {
            return 704073337;
        }

        public String toString() {
            return "optional4j.spec.Null";
        }
    }
}
