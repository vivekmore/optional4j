package poptional.test.model;

import poptional.OptionalType;

import javax.annotation.NonNull;

@OptionalType
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

	@NonNull
	public Year getYearPlain() {
		return this.year;
	}

	public void setYear(Year year) {
		this.year = year;
	}
}
