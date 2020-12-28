package com.petra.ioptional.annotations;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

@Target(TYPE)
public @interface Optionalizable {

	@Target(FIELD)
	@interface Include {

	}

	@Target(FIELD)
	@interface Exclude {

	}
}
