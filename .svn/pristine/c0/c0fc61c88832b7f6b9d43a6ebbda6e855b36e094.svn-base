package org.bigmouth.nvwa.contentstore.redis.codec.tlv;

import org.bigmouth.nvwa.contentstore.redis.codec.BeanCodecFactory;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanDecoder;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanEncoder;

public class TlvBeanCodecFactory implements BeanCodecFactory {

	private final TlvBeanEncoder tlvBeanEncoder;
	private final TlvBeanDecoder tlvBeanDecoder;

	public TlvBeanCodecFactory(TlvBeanEncoder tlvBeanEncoder, TlvBeanDecoder tlvBeanDecoder) {
		super();
		this.tlvBeanEncoder = tlvBeanEncoder;
		this.tlvBeanDecoder = tlvBeanDecoder;
	}

	@Override
	public BeanDecoder getDecoder() {
		return tlvBeanDecoder;
	}

	@Override
	public BeanEncoder getEncoder() {
		return tlvBeanEncoder;
	}
}
