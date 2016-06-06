package org.bigmouth.nvwa.contentstore.redis.codec.serial;

import java.io.Serializable;

import org.bigmouth.nvwa.contentstore.redis.codec.BeanCodecFactory;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanDecodeException;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanDecoder;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanEncodeException;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanEncoder;
import org.bigmouth.nvwa.utils.SerializeUtils;


public final class SerialCodecFactory implements BeanCodecFactory {

	private final SerializeBeanEncoder encoder = new SerializeBeanEncoder();
	private final SerializeBeanDecoder decoder = new SerializeBeanDecoder();

	@Override
	public BeanDecoder getDecoder() {
		return decoder;
	}

	@Override
	public BeanEncoder getEncoder() {
		return encoder;
	}

	private static final class SerializeBeanEncoder implements BeanEncoder {

		@Override
		public byte[] encode(Object bean) throws BeanEncodeException {
			if (null == bean)
				throw new NullPointerException("bean");
			if (!(bean instanceof Serializable))
				throw new IllegalArgumentException("bean expect implement Serializable interface.");

			return SerializeUtils.encode((Serializable) bean);
		}
	}

	private static final class SerializeBeanDecoder implements BeanDecoder {

		@SuppressWarnings("unchecked")
		@Override
		public <T> T decode(byte[] bytes, Class<T> template) throws BeanDecodeException {
			if (null == bytes || 0 == bytes.length)
				throw new IllegalArgumentException("bytes is blank.");
			if (Serializable.class.isAssignableFrom(template))
				throw new IllegalArgumentException("template expect subclass of Serializable,but "
						+ template);

			return (T) SerializeUtils.decode(bytes, (Class) template);
		}
	}
}
