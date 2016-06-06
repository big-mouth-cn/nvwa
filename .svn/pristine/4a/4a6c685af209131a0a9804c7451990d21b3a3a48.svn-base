package org.bigmouth.nvwa.validate.factory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.bigmouth.nvwa.validate.Validator;


/**
 * Only for Validate Annotation.
 * 
 * @author nada
 * 
 */
public abstract class ValidatorMetadata {

	private final Class<? extends Annotation> annoClass;

	public ValidatorMetadata(Class<? extends Annotation> annoClass) {
		this.annoClass = annoClass;
	}

	public Class<? extends Annotation> getAnnoClass() {
		return annoClass;
	}

	public abstract void match(Class<?> clazz);

	public abstract Validator create(Field f, Annotation anno);
}
