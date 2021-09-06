package popnop.test.model;

import popnop.spec.NullObjectType;

@NullObjectType
public class Year {

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
