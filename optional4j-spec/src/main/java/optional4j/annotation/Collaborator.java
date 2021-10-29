package optional4j.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, FIELD, PARAMETER, METHOD, LOCAL_VARIABLE})
@Retention(RUNTIME)
public @interface Collaborator {

	String NULL_OBJECT_FACTORY_METHOD_NAME = "ofNullable";

	String NULL_INSTANCE_FACTORY_METHOD_NAME = "nullInstance";

	String NULL_OBJ_INTERFACE_PREFIX = "I";

	String NULL_OBJ_INTERFACE_POSTFIX = "";

	String NULL_PREFIX = "Null";

	String NULL_POSTFIX = "";


	// prefix for the non-null type name e.g., Foo
	String prefix() default "";

	// postfix for the non-null type name e.g., Foo
	String postfix() default "";

	// prefix for the Nullxxx type name e.g., NullFoo
	String nullPrefix() default NULL_PREFIX;

	// postfix for the Nullxxx type name e.g., NullFoo
	String nullPostfix() default NULL_POSTFIX;

	// prefix for the parent xxxNullObject type name e.g., FooNullObject
	String nullObjectPrefix() default NULL_OBJ_INTERFACE_PREFIX;

	// postfix for the parent xxxNullObject type name e.g., FooNullObject
	String nullObjectPostfix() default NULL_OBJ_INTERFACE_POSTFIX;
}
