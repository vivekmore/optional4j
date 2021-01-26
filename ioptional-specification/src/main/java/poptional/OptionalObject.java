package poptional;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, METHOD })
//@Retention(RUNTIME)
public @interface OptionalObject {

	/**
	 * @return a prefix to add to the generated class name
	 */
	String prefix() default "";

	/**
	 * @return a suffix to add to the generated class name
	 */
	String suffix() default "";

	@Target(METHOD)
	@interface NotNull {

	}
}
