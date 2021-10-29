package optional4j.support;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public enum ModeValue {

    // Apply null safety wherever possible
    PESSIMISTIC,

    // A performant mode which applies the null safety features only wherever applicable.
    OPTIMISTIC;

    public static Set<String> modeValues() {
        return Arrays.stream(ModeValue.values()).map(value -> value.name().toLowerCase()).collect(toSet());
    }
}
