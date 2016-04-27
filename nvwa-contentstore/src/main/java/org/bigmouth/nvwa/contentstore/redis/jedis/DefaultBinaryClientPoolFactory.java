package org.bigmouth.nvwa.contentstore.redis.jedis;

import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

public class DefaultBinaryClientPoolFactory implements BinaryClientPoolFactory<BinaryJedis> {

	private final JedisPoolConfig config;
	private final String host;
	private final int port;

	private final Pool<BinaryJedis> pool;

	public DefaultBinaryClientPoolFactory(JedisPoolConfig config, String host, int port) {
		super();
		if (null == config)
			throw new NullPointerException("config");
		this.config = config;
		this.host = host;
		this.port = port;

		pool = new DefaultBinaryClientPool(config, host, port, 5000);
	}

	@Override
	public Pool<BinaryJedis> create() {
		return pool;
	}

	public JedisPoolConfig getConfig() {
		return config;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
}
