package org.bigmouth.nvwa.validate.factory;

import org.bigmouth.nvwa.validate.ValidateException;

public class ValidatorCreateException extends ValidateException {

	private static final long serialVersionUID = -3903245074199810352L;

	public ValidatorCreateException() {
		super();
	}

	public ValidatorCreateException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidatorCreateException(String message) {
		super(message);
	}

	public ValidatorCreateException(Throwable cause) {
		super(cause);
	}
}
