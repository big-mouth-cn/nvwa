package org.bigmouth.nvwa.contentstore.redis.codec;

public interface BeanCodecFactory {

	BeanEncoder getEncoder();

	BeanDecoder getDecoder();
}
