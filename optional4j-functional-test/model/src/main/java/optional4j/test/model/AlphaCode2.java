package optional4j.test.model;

import optional4j.annotation.ValueType;

import javax.annotation.Nullable;

@ValueType
public class AlphaCode2 {

	private Year year;

	public AlphaCode2() {
	}

	public AlphaCode2(Year year) {
		this.year = year;
	}

	@Nullable
	public Year getYear() {
		return this.year;
	}

	public Year getYearPlain() {
		return this.year;
	}

	public void setYear(Year year) {
		this.year = year;
	}
}
