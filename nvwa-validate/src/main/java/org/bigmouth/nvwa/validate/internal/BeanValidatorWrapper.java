package org.bigmouth.nvwa.validate.internal;

import java.lang.reflect.Field;

import org.bigmouth.nvwa.validate.AbstractValidator;


public class BeanValidatorWrapper extends AbstractValidator {

	private final BeanValidator beanValidator;

	public BeanValidatorWrapper(Field field, BeanValidator beanValidator) {
		super(field);
		if (null == beanValidator)
			throw new NullPointerException("beanValidator");
		this.beanValidator = beanValidator;
	}

	@Override
	protected void doValidate(Object input) throws Exception {
		Object v = getValue(input);
		if (null == v) {
			// TODO:
			return;
		}
		beanValidator.validate(v);
	}

	@Override
	public String getConstraintDesc() {
		return beanValidator.getConstraintDesc();
	}
}
