package poptional.test.model;

import poptional.OptionalType;

@OptionalType
public class Year {

    private Integer value;

    public Year() {
    }

    public Year(Integer value) {
        this.value = value;
    }

    public Integer getValue(){
        return this.value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
