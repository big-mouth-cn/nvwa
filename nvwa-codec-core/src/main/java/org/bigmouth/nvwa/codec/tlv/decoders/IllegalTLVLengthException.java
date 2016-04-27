package org.bigmouth.nvwa.codec.tlv.decoders;

public class IllegalTLVLengthException extends RuntimeException {

	private static final long serialVersionUID = -7014966296622995022L;

	public IllegalTLVLengthException() {
		super();
	}

	public IllegalTLVLengthException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalTLVLengthException(String message) {
		super(message);
	}

	public IllegalTLVLengthException(Throwable cause) {
		super(cause);
	}
}
