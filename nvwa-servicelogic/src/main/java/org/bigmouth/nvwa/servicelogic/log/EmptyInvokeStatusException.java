package org.bigmouth.nvwa.servicelogic.log;

public class EmptyInvokeStatusException extends RuntimeException {

	private static final long serialVersionUID = -7737697102083747942L;

	public EmptyInvokeStatusException() {
		super();
	}

	public EmptyInvokeStatusException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyInvokeStatusException(String message) {
		super(message);
	}

	public EmptyInvokeStatusException(Throwable cause) {
		super(cause);
	}
}
