package org.bigmouth.nvwa.validate.internal;

import java.lang.reflect.Field;

import org.bigmouth.nvwa.validate.AbstractValidator;
import org.bigmouth.nvwa.validate.ConstraintViolationException;


public class NotNullValidator extends AbstractValidator {

	public NotNullValidator(Field field) {
		super(field);
	}

	@Override
	protected void doValidate(Object input) throws Exception {
		Object v = getValue(input);
		if (null == v)
			throw new ConstraintViolationException(this);
	}

	@Override
	public String getConstraintDesc() {
		return getFieldDesc() + " must not be allow null.";
	}
}
