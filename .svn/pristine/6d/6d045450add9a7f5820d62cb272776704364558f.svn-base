package org.bigmouth.nvwa.contentstore.redis.codec.tlv;

import java.util.List;

import org.bigmouth.nvwa.codec.tlv.TLVEncoder;
import org.bigmouth.nvwa.codec.tlv.TLVEncoderProvider;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanEncodeException;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanEncoder;
import org.bigmouth.nvwa.utils.ByteUtils;


public final class TlvBeanEncoder implements BeanEncoder {

	private final TLVEncoderProvider tlvEncoderProvider;

	public TlvBeanEncoder(TLVEncoderProvider tlvEncoderProvider) {
		super();
		this.tlvEncoderProvider = tlvEncoderProvider;
	}

	@Override
	public byte[] encode(Object bean) throws BeanEncodeException {
		if (null == bean)
			throw new NullPointerException("bean");
		TLVEncoder<Object> tlvEncoder = tlvEncoderProvider.getObjectEncoder();
		List<byte[]> bytesList = tlvEncoder.codec(bean, null);
		return ByteUtils.union(bytesList);
	}
}
