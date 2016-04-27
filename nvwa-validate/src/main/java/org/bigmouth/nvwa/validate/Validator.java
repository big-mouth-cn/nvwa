package org.bigmouth.nvwa.validate;

public interface Validator {

	void validate(Object input) throws ValidateException, ConstraintViolationException;

	String getConstraintDesc();
}
