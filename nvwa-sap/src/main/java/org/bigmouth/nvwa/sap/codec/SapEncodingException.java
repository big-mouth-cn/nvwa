package org.bigmouth.nvwa.sap.codec;

import org.apache.mina.filter.codec.ProtocolDecoderException;

public class SapEncodingException extends ProtocolDecoderException {

	private static final long serialVersionUID = 1L;

	public SapEncodingException() {
		super();
	}

	public SapEncodingException(String message, Throwable cause) {
		super(message, cause);
	}

	public SapEncodingException(String message) {
		super(message);
	}

	public SapEncodingException(Throwable cause) {
		super(cause);
	}
}
