package org.bigmouth.nvwa.contentstore.redis.jedis;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.contentstore.impl.ContentStoreUtils;
import org.bigmouth.nvwa.contentstore.redis.AbstractRedisContentStore;
import org.bigmouth.nvwa.contentstore.redis.RedisOperationException;
import org.bigmouth.nvwa.contentstore.redis.codec.BeanCodecFactory;
import org.bigmouth.nvwa.utils.ByteUtils;
import org.bigmouth.nvwa.utils.HexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.BinaryJedis;
import redis.clients.util.Pool;

import com.google.common.collect.Lists;

public class JedisContentStore<T extends BinaryJedis> extends AbstractRedisContentStore {

	private static final Logger LOGGER = LoggerFactory.getLogger(JedisContentStore.class);
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final int DUMP_BYTE_COUNT = 16;

	private final BinaryClientPoolFactory<T> clientFactory;

	public JedisContentStore(BeanCodecFactory codecFactory, BinaryClientPoolFactory<T> clientFactory) {
		super(codecFactory);
		if (null == clientFactory)
			throw new NullPointerException("clientFactory");
		this.clientFactory = clientFactory;
	}

	private Pool<T> getPool() {
		return clientFactory.create();
	}

	@Override
	public boolean contains(String key) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		T client = null;
		try {
			client = getClient();
			return client.exists(getKey(key));
		} catch (Exception e) {

			if (null != client) {
				client.quit();
				returnBrokenResource(client);
			}
			throw new RedisOperationException("contains:", e);
		} finally {
			if (null != client) {
				returnResource(client);
			}
		}
	}

	private void returnResource(T client) {
		if (null != client) {
			getPool().returnResource(client);
		}
	}

	private void returnBrokenResource(T client) {
		if (null != client) {
			client.quit();
			getPool().returnBrokenResource(client);
		}
	}

	private String dump(byte[] bs) {
		StringBuilder sb = new StringBuilder(128);
		sb.append("[");
		String ret = HexUtils.bytesAsHexCodes(bs, DUMP_BYTE_COUNT);
		sb.append(ret);
		if ((bs.length * 3 - 1) > ret.length())
			sb.append("...");
		sb.append("]");
		return sb.toString();
	}

	@Override
	public void appendListHead(String key, byte[] value) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == value)
			throw new NullPointerException("value");
		if (0 == value.length)
			throw new IllegalArgumentException("value is blank.");
		T client = null;
		try {
			client = getClient();
			client.lpush(getKey(key), value);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("One item{} has been added for list-head[{}] to Redis.", dump(value),
						key);
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("appendList:", e);
		} finally {
			returnResource(client);
		}
	}

	@Override
	public void appendListTail(String key, byte[] value) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == value)
			throw new NullPointerException("value");
		if (0 == value.length)
			throw new IllegalArgumentException("value is blank.");
		T client = null;
		try {
			client = getClient();
			client.rpush(getKey(key), value);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("One item{} has been added for list-tail[{}] to Redis.", dump(value),
						key);
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("appendList:", e);
		} finally {
			returnResource(client);
		}
	}

	@Deprecated
	@Override
	public void appendList(String key, byte[] value) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == value)
			throw new NullPointerException("value");
		if (0 == value.length)
			throw new IllegalArgumentException("value is blank.");
		T client = null;
		try {
			client = getClient();
			client.rpush(getKey(key), value);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("One item{} has been added for list-tail[{}] to Redis.", dump(value),
						key);
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("appendList:", e);
		} finally {
			returnResource(client);
		}
	}

	private T getClient() {
		T client;
		client = getPool().getResource();
		return client;
	}

	@Override
	public byte[] fetch(String key) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		T client = null;
		try {
			client = getClient();
			byte[] ret = client.get(getKey(key));
			if (null == ret || 0 == ret.length) {
				if (LOGGER.isWarnEnabled())
					LOGGER.warn("Get blank value from Redis for key[{}].", key);
				return new byte[0];
			}
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Get item{} from Redis for key[{}]", dump(ret), key);
			return ret;
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("fetch:", e);
		} finally {
			returnResource(client);
		}
	}

	@Override
	public List<byte[]> listRange(String key, int begin, int end) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (begin < 0)
			throw new IllegalArgumentException("begin:" + begin);
		if (end < 0)
			throw new IllegalArgumentException("end:" + end);
		if (begin > end)
			throw new IllegalArgumentException("begin:" + begin + " end:" + end + ".");
		T client = null;
		try {
			client = getClient();
			List<byte[]> list = client.lrange(getKey(key), begin, end);
			if (null == list || 0 == list.size()) {
				if (LOGGER.isWarnEnabled())
					LOGGER.warn("Get blank list from Redis for key[{}].", key);
				return Lists.newArrayList();
			}
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Get list[size={}] from Redis for key[{}].", list.size(), key);

			return list;
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("listRange:", e);
		} finally {
			returnResource(client);
		}
	}

	@Override
	public boolean remove(String key) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		T client = null;
		try {
			client = getClient();
			long ret = client.del(getKey(key));
			if (ret > 0) {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug("Remove item from Redis for key[{}].", key);
			} else {
				if (LOGGER.isDebugEnabled())
					LOGGER.debug(
							"Remove item from Redis for key[{}] !Failed!,maybe its value is not exists.",
							key);
			}
			return ret > 0;
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("remove:", e);
		} finally {
			returnResource(client);
		}
	}

	@Override
	public void store(String key, byte[] value) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == value)
			throw new NullPointerException("value");
		if (0 == value.length) {
			if (LOGGER.isWarnEnabled())
				LOGGER.warn("JedisContentStore.store,value is blank.");
		}
		T client = null;
		try {
			client = getClient();
			// TODO:how to handle statusCode?
			String statusCode = client.set(getKey(key), value);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Store item{} for key[{}] to Redis.", dump(value), key);
			}
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("store:", e);
		} finally {
			returnResource(client);
		}
	}

	@Override
	public long listLength(String key) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		T client = null;
		try {
			client = getClient();
			return client.llen(getKey(key));
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("listLength:", e);
		} finally {
			returnResource(client);
		}
	}

	private byte[] getKey(String key) throws UnsupportedEncodingException {
		return key.getBytes(DEFAULT_CHARSET);
	}

	private String content2key(byte[] content) {
		return ContentStoreUtils.content2key(content);
	}

	@Override
	public String store(byte[] value) {
		if (null == value)
			throw new NullPointerException("value");
		String key = content2key(value);
		this.store(key, value);
		return key;
	}

	@Override
	public String store(List<?> value) {
		if (null == value)
			throw new NullPointerException("value");
		String key = list2Key(value);
		this.store(key, value);
		return key;
	}

	private String list2Key(List<?> value) {
		List<byte[]> ret = Lists.newArrayList();
		for (Object bean : value) {
			if (bean instanceof byte[]) {
				ret.add((byte[]) bean);
			} else {
				byte[] bytes = encode(bean);
				ret.add(bytes);
			}
		}
		byte[] bs = ByteUtils.union(ret);
		return content2key(bs);
	}

	private void removeItemFromList(String key, byte[] value, int matchCount) {
		T client = null;
		try {
			client = getClient();
			// TODO:how to handle statusCode?
			long actualCount = client.lrem(getKey(key), matchCount, value);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Remove item count[" + actualCount
						+ "] of list[{}] for item value[{}] from Redis.", key, dump(value));
			}
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("store:", e);
		} finally {
			returnResource(client);
		}
	}

	@Override
	public void removeItemFromListTail(String key, byte[] value, int matchCount) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == value)
			throw new NullPointerException("value");
		if (0 == value.length)
			throw new IllegalArgumentException("value is blank.");
		if (matchCount < 0)
			matchCount = 0;

		removeItemFromList(key, value, (0 - matchCount));
	}

	@Override
	public void removeItemFromListHead(String key, byte[] value, int matchCount) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (null == value)
			throw new NullPointerException("value");
		if (0 == value.length)
			throw new IllegalArgumentException("value is blank.");
		if (matchCount < 0)
			matchCount = 0;

		removeItemFromList(key, value, matchCount);
	}

	@Override
	public long incrementAndGet(String key, long step) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (step <= 0)
			throw new IllegalArgumentException("step must be greater than zero.");
		T client = null;
		try {
			client = getClient();
			long ret = client.incrBy(getKey(key), step);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Return long{} from Redis for INCR key[{}]", ret, key);
			return ret;
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("incr:", e);
		} finally {
			returnResource(client);
		}
	}

	@Override
	public long decrementAndGet(String key, long step) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		if (step <= 0)
			throw new IllegalArgumentException("step must be greater than zero.");
		T client = null;
		try {
			client = getClient();
			long ret = client.decrBy(getKey(key), step);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Return Long{} from Redis for DECR key[{}]", ret, key);
			return ret;
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("decr:", e);
		} finally {
			returnResource(client);
		}
	}

	@Override
	public long set(String key, long value) {
		throw new UnsupportedOperationException("Unimplement.");
	}

	@Override
	public long get(String key) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		T client = null;
		try {
			client = getClient();
			long ret = client.incrBy(getKey(key), 0);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Return long{} from Redis for get key[{}]", ret, key);
			return ret;
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("get:", e);
		} finally {
			returnResource(client);
		}
	}

	@Override
	public void expire(String key, int timeout) {
		T client = null;
		try {
			client = getClient();
			client.expire(getKey(key), timeout);
		} catch (Exception e) {
			returnBrokenResource(client);
			throw new RedisOperationException("expire:", e);
		} finally {
			returnResource(client);
		}
	}
}
