package org.bigmouth.nvwa.validate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bigmouth.nvwa.validate.NoopValidator;
import org.bigmouth.nvwa.validate.Validator;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateCustom {

	Class<? extends Validator> validator() default NoopValidator.class;
}
