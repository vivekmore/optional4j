package poptional;

public class SomethingImpl<T> implements Something<T> {

	private final T value;

	public SomethingImpl(T value) {
		this.value = value;
	}

	@Override
	public T get() {
		return value;
	}
}
