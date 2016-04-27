package org.bigmouth.nvwa.cluster.node;

public class NoSuchPathException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoSuchPathException() {
		super();
	}

	public NoSuchPathException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchPathException(String message) {
		super(message);
	}

	public NoSuchPathException(Throwable cause) {
		super(cause);
	}
}
