package optional4j.test.model;

import static optional4j.support.ModeValue.PESSIMISTIC;

import javax.annotation.Nullable;
import optional4j.annotation.Mode;
import optional4j.annotation.Optional4J;

@Optional4J
public class AlphaCode2 {

    private Year year;

    public AlphaCode2() {}

    public AlphaCode2(Year year) {
        this.year = year;
    }

    @Nullable
    @Mode(PESSIMISTIC)
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
