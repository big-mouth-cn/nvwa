package org.bigmouth.nvwa.sap.codec;

import org.apache.mina.filter.codec.ProtocolDecoderException;

public class SapDecoderException extends ProtocolDecoderException {

	private static final long serialVersionUID = 1L;

	public SapDecoderException() {
		super();
	}

	public SapDecoderException(String message, Throwable cause) {
		super(message, cause);
	}

	public SapDecoderException(String message) {
		super(message);
	}

	public SapDecoderException(Throwable cause) {
		super(cause);
	}
}
