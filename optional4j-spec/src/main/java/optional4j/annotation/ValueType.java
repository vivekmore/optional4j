package optional4j.annotation;

import optional4j.support.ModeValue;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface ValueType {

	/**
	 * @return a prefix to add to the generated class name
	 */
	String prefix() default "";

	/**
	 * @return a suffix to add to the generated class name
	 */
	String suffix() default "";

	ModeValue mode() default ModeValue.OPTIMISTIC;
}