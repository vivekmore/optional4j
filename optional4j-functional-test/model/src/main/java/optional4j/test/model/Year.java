package optional4j.test.model;

import javax.annotation.Nullable;
import optional4j.annotation.Optional4J;

@Optional4J
public class Year {

    private Integer value;

    public Year() {}

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
