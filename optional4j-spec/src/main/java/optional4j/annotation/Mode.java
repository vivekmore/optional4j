package optional4j.annotation;

import optional4j.support.ModeValue;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static optional4j.support.ModeValue.OPTIMISTIC;

@Retention(RUNTIME)
public @interface Mode {

	ModeValue value() default OPTIMISTIC;
}
