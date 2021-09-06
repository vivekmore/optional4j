package popnop.spec;

public final class SomethingImpl<T> extends Something<T> {

    private final T value;

    public SomethingImpl(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }
}
