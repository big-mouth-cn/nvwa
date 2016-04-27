package org.bigmouth.nvwa.validate.internal;

import java.lang.reflect.Field;

import org.bigmouth.nvwa.validate.AbstractValidator;
import org.bigmouth.nvwa.validate.Validator;


public class CustomValidator extends AbstractValidator {

	private final Validator custom;

	public CustomValidator(Field field, Validator custom) {
		super(field);
		if (null == custom)
			throw new NullPointerException("CustomValidator");
		this.custom = custom;
	}

	@Override
	protected void doValidate(Object input) throws Exception {
		Object v = getValue(input);
		custom.validate(v);
	}

	@Override
	public String getConstraintDesc() {
		return custom.getConstraintDesc();
	}
}
