package org.bigmouth.nvwa.sap;

public class IllegalExtendedItemException extends RuntimeException {

	private static final long serialVersionUID = 4979568930705786715L;

	public IllegalExtendedItemException() {
		super();
	}

	public IllegalExtendedItemException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalExtendedItemException(String message) {
		super(message);
	}

	public IllegalExtendedItemException(Throwable cause) {
		super(cause);
	}
}
