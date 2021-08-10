package poptional.test.model;
import poptional.OptionalObject;
import poptional.Something;
@OptionalObject
public final class Year extends Something<Year> {
    private Integer value;

    public Year() {
    }

    public Year(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}