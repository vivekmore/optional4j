package optional4j.test.model;

import optional4j.annotation.ValueType;

import javax.annotation.Nullable;

@ValueType
public class Year {

	private Integer value;

	public Year() {
	}

	public Year(Integer value) {
		this.value = value;
	}

	@Nullable
	public Integer getValue() {
		return this.value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
