package org.bigmouth.nvwa.contentstore.redis.codec;

public class BeanEncodeException extends RuntimeException {

	private static final long serialVersionUID = -2948771405744918233L;

	public BeanEncodeException() {
		super();
	}

	public BeanEncodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanEncodeException(String message) {
		super(message);
	}

	public BeanEncodeException(Throwable cause) {
		super(cause);
	}
}
