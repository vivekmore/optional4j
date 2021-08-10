package poptional.test.model;

import poptional.OptionalObject;

@OptionalObject
public class AlphaCode2 {

	private Year year;

	public AlphaCode2() {
	}

	public AlphaCode2(Year year) {
		this.year = year;
	}

	public Year getYear() {
		return this.year;
	}

	@OptionalObject.NotNull
	public Year getYearPlain() {
		return this.year;
	}

	public void setYear(Year year) {
		this.year = year;
	}
}
