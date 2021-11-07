package optional4j.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static optional4j.support.ModeValue.OPTIMISTIC;

import java.lang.annotation.Retention;
import optional4j.support.ModeValue;

@Retention(RUNTIME)
public @interface Mode {

    ModeValue value() default OPTIMISTIC;
}
