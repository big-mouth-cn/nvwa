package com.skymobi.market.commons.validate;

import static junit.framework.Assert.assertTrue;

import org.bigmouth.nvwa.validate.ConstraintViolationException;
import org.bigmouth.nvwa.validate.Validator;

public class ValidateTestSupport {

	protected void validateViolateConstraint(Validator validator, Object bean) {
		try {
			validator.validate(bean);
			assertTrue(false);
		} catch (Exception e) {
			if (e instanceof ConstraintViolationException) {
				e.printStackTrace();
				assertTrue(true);
			} else
				assertTrue(false);
		}
	}

	protected void validatePass(Validator validator, Object bean) {
		try {
			validator.validate(bean);
			assertTrue(true);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
}
