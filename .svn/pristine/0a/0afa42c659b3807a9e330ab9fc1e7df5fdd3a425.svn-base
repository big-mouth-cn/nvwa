package org.bigmouth.nvwa.contentstore.redis.codec.tlv;

import org.bigmouth.nvwa.codec.tlv.TLVDecoder;
import org.bigmouth.nvwa.codec.tlv.TLVDecoderProvider;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanDecodeException;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanDecoder;


public final class TlvBeanDecoder implements BeanDecoder {

	private final TLVDecoderProvider tlvDecoderProvider;

	public TlvBeanDecoder(TLVDecoderProvider tlvDecoderProvider) {
		super();
		this.tlvDecoderProvider = tlvDecoderProvider;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T decode(byte[] bytes, Class<T> template) throws BeanDecodeException {
		if (null == bytes || 0 == bytes.length)
			throw new NullPointerException("bytes is blank.");
		if (null == template)
			throw new NullPointerException("template");
		TLVDecoder<Object> tlvDecoder = tlvDecoderProvider.getObjectDecoder();
		return (T) tlvDecoder.codec(bytes, template);
	}
}
