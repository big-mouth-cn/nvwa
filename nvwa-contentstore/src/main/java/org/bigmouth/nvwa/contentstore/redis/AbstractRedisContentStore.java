package org.bigmouth.nvwa.contentstore.redis;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanCodecFactory;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanDecoder;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public abstract class AbstractRedisContentStore implements RedisTypeContentStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRedisContentStore.class);
	private final BeanCodecFactory codecFactory;

	public AbstractRedisContentStore(BeanCodecFactory codecFactory) {
		if (null == codecFactory)
			throw new NullPointerException("codecFactory");
		this.codecFactory = codecFactory;
	}

	private BeanEncoder getEncoder() {
		BeanEncoder encoder = codecFactory.getEncoder();
		if (null == encoder)
			throw new NullPointerException("codecFactory.getEncoder");
		return encoder;
	}

	private BeanDecoder getDecoder() {
		BeanDecoder decoder = codecFactory.getDecoder();
		if (null == decoder)
			throw new NullPointerException("codecFactory.getDecoder");
		return decoder;
	}

	protected byte[] encode(Object bean) {
		if (null == bean)
			throw new NullPointerException("bean");
		return getEncoder().encode(bean);
	}

	protected <T> T decode(byte[] bytes, Class<T> template) {
		if (null == bytes)
			throw new NullPointerException("bytes");
		return getDecoder().decode(bytes, template);
	}

	@Override
	public void appendListHead(String key, Object value) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == value)
			throw new NullPointerException("value");
		if (value instanceof byte[]) {
			this.appendListHead(key, (byte[]) value);
		} else {
			byte[] bv = encode(value);
			this.appendListHead(key, bv);
		}
	}

	@Override
	public void appendListTail(String key, Object value) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == value)
			throw new NullPointerException("value");
		if (value instanceof byte[]) {
			this.appendListTail(key, (byte[]) value);
		} else {
			byte[] bv = encode(value);
			this.appendListTail(key, bv);
		}
	}

	@Deprecated
	@Override
	public void appendList(String key, Object value) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == value)
			throw new NullPointerException("value");
		if (value instanceof byte[]) {
			this.appendList(key, (byte[]) value);
		} else {
			byte[] bv = encode(value);
			this.appendList(key, bv);
		}
	}

	@Override
	public <T> T fetch(String key, Class<T> clazz) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == clazz)
			throw new NullPointerException("class");
		byte[] bv = fetch(key);
		if (null == bv || 0 == bv.length)
			throw new RedisOperationException("Fetch bytes is blank,can not to cast class.");
		Object ret = decode(bv, clazz);
		if (null == ret)
			throw new RedisOperationException("decode result is null.");
		return clazz.cast(ret);
	}

	@Override
	public <T> List<T> listRange(String key, int begin, int end, Class<T> elementClass) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (begin < 0)
			throw new IllegalArgumentException("begin:" + begin);
		if (end < 0)
			throw new IllegalArgumentException("end:" + end);
		if (begin > end)
			throw new IllegalArgumentException("begin:" + begin + " end:" + end + ".");
		List<byte[]> bvList = listRange(key, begin, end);
		List<T> ret = Lists.newArrayList();
		for (byte[] bv : bvList) {
			T bean = (T) decode(bv, elementClass);
			ret.add(bean);
		}
		return ret;
	}

	@Override
	public void store(String key, Object value) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == value)
			throw new NullPointerException("value");
		if (value instanceof byte[]) {
			this.store(key, (byte[]) value);
		} else {
			byte[] bv = encode(value);
			this.store(key, bv);
		}
	}

	@Override
	public String store(Object value) {
		if (null == value)
			throw new NullPointerException("value");
		if (value instanceof byte[]) {
			return this.store((byte[]) value);
		}
		// TODO:if value is List?
		byte[] bv = encode(value);
		if (null == bv || 0 == bv.length)
			throw new RedisOperationException("Encode value[" + value + "] result is blank.");
		return this.store(bv);
	}

	@Override
	public void store(String key, List<?> values) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == values)
			throw new NullPointerException("values");
		if (0 == values.size())
			throw new IllegalArgumentException("values is blank.");

		if (contains(key)) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Content for key [" + key + "] already exist. just ingore");
			return;
		}

		for (Object bean : values) {
			this.appendList(key, bean);
		}
	}
}
