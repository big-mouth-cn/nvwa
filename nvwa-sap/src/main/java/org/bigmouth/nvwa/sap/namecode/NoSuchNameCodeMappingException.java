package org.bigmouth.nvwa.sap.namecode;

public class NoSuchNameCodeMappingException extends Exception {

	private static final long serialVersionUID = -8275619218518304086L;

	public NoSuchNameCodeMappingException() {
		super();
	}

	public NoSuchNameCodeMappingException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchNameCodeMappingException(String message) {
		super(message);
	}

	public NoSuchNameCodeMappingException(Throwable cause) {
		super(cause);
	}
}
