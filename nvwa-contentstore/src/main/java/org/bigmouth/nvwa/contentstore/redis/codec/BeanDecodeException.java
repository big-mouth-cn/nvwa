package org.bigmouth.nvwa.contentstore.redis.codec;

public class BeanDecodeException extends RuntimeException {

	private static final long serialVersionUID = 8902431813664126150L;

	public BeanDecodeException() {
		super();
	}

	public BeanDecodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public BeanDecodeException(String message) {
		super(message);
	}

	public BeanDecodeException(Throwable cause) {
		super(cause);
	}
}
