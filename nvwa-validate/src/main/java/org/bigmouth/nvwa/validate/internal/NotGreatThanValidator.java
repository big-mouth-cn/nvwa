package org.bigmouth.nvwa.validate.internal;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.bigmouth.nvwa.validate.CompareValidator;
import org.bigmouth.nvwa.validate.ConstraintViolationException;
import org.bigmouth.nvwa.validate.ValidateException;


public class NotGreatThanValidator extends CompareValidator {

	public NotGreatThanValidator(Field field, Field compareField) {
		super(field, compareField);
	}

	@Override
	protected void doValidate(Object input) throws Exception {

		long v0 = getValueL(input);
		long v1 = getCompareValueL(input);
		if (v0 > v1) {
			throw new ConstraintViolationException(this, v0 + "," + v1);
		}
	}

	private long getCompareValueL(Object input) {
		try {
			Number v = (Number) PropertyUtils.getProperty(input, getCompareFieldName());
			return v.longValue();
		} catch (IllegalAccessException e) {
			throw new ValidateException("getCompareValueL:", e);
		} catch (InvocationTargetException e) {
			throw new ValidateException("getCompareValueL:", e);
		} catch (NoSuchMethodException e) {
			throw new ValidateException("getCompareValueL:", e);
		}
	}

	private long getValueL(Object input) {
		try {
			Number v = (Number) PropertyUtils.getProperty(input, getFieldName());
			return v.longValue();
		} catch (IllegalAccessException e) {
			throw new ValidateException("getValueL:", e);
		} catch (InvocationTargetException e) {
			throw new ValidateException("getValueL:", e);
		} catch (NoSuchMethodException e) {
			throw new ValidateException("getValueL:", e);
		}
	}

	private String getCompareFieldDesc() {
		return getFieldDesc(getCompareField());
	}

	@Override
	public String getConstraintDesc() {
		return new StringBuilder(64).append(getFieldDesc()).append(" must not be great than ")
				.append(getCompareFieldDesc()).toString();
	}
}
