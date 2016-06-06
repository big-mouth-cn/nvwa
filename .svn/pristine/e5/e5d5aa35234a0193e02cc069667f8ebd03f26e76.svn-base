package org.bigmouth.nvwa.codec.tlv;

import org.bigmouth.nvwa.codec.Codec;
import org.bigmouth.nvwa.codec.error.IllegalTLVContentHandler;

public interface TLVDecoder<R extends Object> extends Codec<byte[], R, TLVDecoderProvider, Object> {

	/**
	 * @deprecated
	 * @return
	 */
	boolean isObjectDecoder();

	void setIllegalTLVContentHandler(IllegalTLVContentHandler illegalTLVContentHandler);
}
