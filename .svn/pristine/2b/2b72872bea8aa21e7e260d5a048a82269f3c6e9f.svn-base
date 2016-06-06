package org.bigmouth.nvwa.validate;

import org.apache.commons.lang.StringUtils;

public class ConstraintViolationException extends ValidateException {

	private static final long serialVersionUID = 7012026401290474475L;

	private final String constraintDesc;

	public ConstraintViolationException(Validator validator, String message, Throwable cause) {
		super(message, cause);
		if (null == validator)
			throw new NullPointerException("validator");
		if (StringUtils.isBlank(validator.getConstraintDesc()))
			throw new IllegalArgumentException("constraintDesc is blank.");
		this.constraintDesc = validator.getConstraintDesc();
	}

	public ConstraintViolationException(Validator validator) {
		this(validator, "");
	}

	public ConstraintViolationException(Validator validator, String message) {
		super(message);
		if (null == validator)
			throw new NullPointerException("validator");
		if (StringUtils.isBlank(validator.getConstraintDesc()))
			throw new IllegalArgumentException("constraintDesc is blank.");
		this.constraintDesc = validator.getConstraintDesc();
	}

	public String getConstraintDesc() {
		return constraintDesc;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder(64).append("Violate Constraint:").append(
				constraintDesc);
		if (StringUtils.isNotBlank(super.getMessage()))
			sb.append(", details:").append(super.getMessage());
		return sb.toString();
	}
}
