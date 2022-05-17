package optional4j.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static optional4j.support.ModeValue.OPTIMISTIC;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import optional4j.support.ModeValue;

@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface Optional4J {

    /** @return a prefix to add to the generated class name */
    String prefix() default "";

    /** @return a suffix to add to the generated class name */
    String suffix() default "";

    ModeValue mode() default OPTIMISTIC;
}
