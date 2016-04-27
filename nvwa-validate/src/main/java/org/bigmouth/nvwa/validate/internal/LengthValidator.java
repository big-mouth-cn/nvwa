package org.bigmouth.nvwa.validate.internal;

import java.lang.reflect.Field;

import org.bigmouth.nvwa.validate.AbstractValidator;
import org.bigmouth.nvwa.validate.ConstraintViolationException;
import org.bigmouth.nvwa.validate.ValidateException;


public class LengthValidator extends AbstractValidator {

	private final int minLen;
	private final int maxLen;

	public LengthValidator(Field field, int minLen, int maxLen) {
		super(field);
		if (minLen < 0)
			throw new IllegalArgumentException("minLen:" + minLen);
		if (minLen > maxLen)
			throw new IllegalArgumentException("minLen:" + minLen + " maxLen:" + maxLen);
		this.minLen = minLen;
		this.maxLen = maxLen;
	}

	@Override
	protected void doValidate(Object input) throws Exception {
		Object v = getValue(input);
		if (null == v)
			throw new ConstraintViolationException(this, "null");
		if (!(v instanceof String))
			throw new ValidateException("Expect String,but " + v);
		String sv = (String) v;
		int l = sv.length();
		if (l < minLen || l > maxLen)
			throw new ConstraintViolationException(this, "value:" + sv);
	}

	@Override
	public String getConstraintDesc() {
		return getFieldDesc() + " length range:[" + minLen + "," + maxLen + "]";
	}
}
