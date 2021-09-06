package popnop.test.model;

import popnop.spec.NullObjectType;
import javax.annotation.NonNull;

@NullObjectType
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
