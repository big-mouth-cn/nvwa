package org.bigmouth.nvwa.contentstore.redis.codec;

public interface BeanEncoder {

	byte[] encode(Object bean) throws BeanEncodeException;
}
