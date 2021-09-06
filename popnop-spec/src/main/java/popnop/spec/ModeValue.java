package popnop.spec;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public enum ModeValue {


    FULL,

    // A performant mode which applies the null safety features only wherever applicable.
    STRICT;

    public static Set<String> modeValues() {
        return Arrays.stream(ModeValue.values()).map(value -> value.name().toLowerCase()).collect(toSet());
    }
}
