package optional4j.annotation;

import static java.lang.annotation.ElementType.LOCAL_VARIABLE;

import java.lang.annotation.Target;

@Target(LOCAL_VARIABLE)
public @interface NullSafe {}
