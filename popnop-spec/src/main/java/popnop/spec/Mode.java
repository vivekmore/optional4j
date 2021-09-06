package popnop.spec;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
public @interface Mode {
    ModeValue value() default ModeValue.STRICT;
}
