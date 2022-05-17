package optional4j.support;

import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Set;

public enum ModeValue {

    /**
     * Apply null safety wherever possible. Not just to {@link optional4j.annotation.Optional4J}
     * annotated types
     */
    PESSIMISTIC,

    /**
     * A performant mode which applies the null safety features only to {@link
     * optional4j.annotation.Optional4J} annotated types
     */
    OPTIMISTIC;

    public static Set<String> modeValues() {
        return Arrays.stream(ModeValue.values())
                .map(value -> value.name().toLowerCase())
                .collect(toSet());
    }
}
