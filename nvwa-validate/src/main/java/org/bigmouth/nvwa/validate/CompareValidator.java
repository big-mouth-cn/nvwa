package org.bigmouth.nvwa.validate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

public abstract class CompareValidator extends AbstractValidator {

	private final Field compareField;
	private final String compareFieldName;

	public CompareValidator(Field field, Field compareField) {
		super(field);
		if (null == compareField)
			throw new NullPointerException("compareField");
		this.compareField = compareField;
		this.compareFieldName = compareField.getName();
	}

	protected Field getCompareField() {
		return compareField;
	}

	public String getCompareFieldName() {
		return compareFieldName;
	}

	protected Object getCompareValue(Object input) throws IllegalArgumentException,
			IllegalAccessException {
		try {
			return PropertyUtils.getProperty(input, compareFieldName);
		} catch (InvocationTargetException e) {
			throw new ValidateException("getTargetValue:", e);
		} catch (NoSuchMethodException e) {
			throw new ValidateException("getTargetValue:", e);
		}
	}
}
