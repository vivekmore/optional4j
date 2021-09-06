package popnop.spec;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface NullObjectType {

    // postfix for the non-null type name e.g., Foo
    String postfix() default "";

    // prefix for the non-null type name e.g., Foo
    String prefix() default "";

    // postfix for the Nullxxx type name e.g., NullFoo
    String nullPostfix() default "Null";

    // prefix for the Nullxxx type name e.g., NullFoo
    String nullPrefix() default "";

    // postfix for the parent xxxNullObject type name e.g., FooNullObject
    String nullObjectPostfix() default "NullObject";

    // prefix for the parent xxxNullObject type name e.g., FooNullObject
    String nullObjectPrefix() default "";
}
