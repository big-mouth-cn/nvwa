package org.bigmouth.nvwa.validate.internal;

import java.lang.reflect.Field;

import org.apache.commons.lang.math.NumberUtils;
import org.bigmouth.nvwa.validate.AbstractValidator;
import org.bigmouth.nvwa.validate.ConstraintViolationException;


public class NumericValidator extends AbstractValidator {

	private final static long DEFAULT_MIN = Long.MIN_VALUE;
	private final static long DEFAULT_MAX = Long.MAX_VALUE;

	private final long min;
	private final long max;

	public NumericValidator(Field field) {
		this(field, DEFAULT_MIN, DEFAULT_MAX);
	}

	public NumericValidator(Field field, long min, long max) {
		super(field);
		if (min > max)
			throw new IllegalArgumentException("min:" + min + " max:" + max);
		this.min = min;
		this.max = max;
	}

	@Override
	protected void doValidate(Object input) throws Exception {
		String v = String.valueOf(getValue(input));
		if (!NumberUtils.isNumber(v))
			throw new ConstraintViolationException(this, v);
		long lv = NumberUtils.toLong(v);
		if (lv < min || lv > max)
			throw new ConstraintViolationException(this, v);
	}

	@Override
	public String getConstraintDesc() {
		return getFieldDesc() + " must be numeric,value range:[" + min + "," + max + "]";
	}
}
