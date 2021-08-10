package poptional.test.model;
import poptional.OptionalObject;
import poptional.Poptional;
import poptional.Something;
@OptionalObject
public final class AlphaCode2 extends Something<AlphaCode2> {
    private Year year;

    public AlphaCode2() {
    }

    public AlphaCode2(Year year) {
        this.year = year;
    }

    public Poptional<Year> getYear() {
        return (this.year) == null? poptional.Poptional.empty(): this.year;
    }

    @OptionalObject.NotNull
    public Year getYearPlain() {
        return this.year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}