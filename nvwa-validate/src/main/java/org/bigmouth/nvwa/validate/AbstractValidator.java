package org.bigmouth.nvwa.validate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

public abstract class AbstractValidator implements Validator {

	private final Field field;
	private final String fieldName;

	public AbstractValidator(Field field) {
		super();
		if (null == field)
			throw new NullPointerException("field");
		this.field = field;
		this.fieldName = field.getName();
	}

	protected String getFieldDesc(Field f) {
		return new StringBuilder(64).append(f.getDeclaringClass().getName()).append(".").append(
				getField().getName()).toString();
	}

	protected String getFieldDesc() {
		return getFieldDesc(getField());
	}

	@Override
	public void validate(Object input) throws ValidateException, ConstraintViolationException {
		if (null == input)
			throw new NullPointerException("input");
		try {
			doValidate(input);
		} catch (Exception e) {
			if (e instanceof ValidateException)
				throw (ValidateException) e;
			throw new ValidateException(e);
		}
	}

	protected abstract void doValidate(Object input) throws Exception;

	protected Object getValue(Object input) throws IllegalArgumentException, IllegalAccessException {
		try {
			return PropertyUtils.getProperty(input, fieldName);
		} catch (InvocationTargetException e) {
			throw new ValidateException("getValue:", e);
		} catch (NoSuchMethodException e) {
			throw new ValidateException("getValue:", e);
		}
	}

	protected Field getField() {
		return field;
	}

	public String getFieldName() {
		return fieldName;
	}

	@Override
	public String toString() {
		return this.getConstraintDesc();
	}
}
