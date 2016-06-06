package org.bigmouth.nvwa.contentstore.redis.jedis;

import org.apache.commons.pool.BasePoolableObjectFactory;

import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
import redis.clients.util.Pool;

public class DefaultBinaryClientPool extends Pool<BinaryJedis> {

	public DefaultBinaryClientPool(final JedisPoolConfig poolConfig, final String host) {
		this(poolConfig, host, Protocol.DEFAULT_PORT, Protocol.DEFAULT_TIMEOUT, null);
	}

	public DefaultBinaryClientPool(String host, int port) {
		super(new JedisPoolConfig(), new BinaryJedisFactory(host, port, Protocol.DEFAULT_TIMEOUT,
				null));
	}

	public DefaultBinaryClientPool(final JedisPoolConfig poolConfig, final String host, int port,
			int timeout, final String password) {
		super(poolConfig, new BinaryJedisFactory(host, port, timeout, password));
	}

	public DefaultBinaryClientPool(final JedisPoolConfig poolConfig, final String host,
			final int port) {
		this(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT, null);
	}

	public DefaultBinaryClientPool(final JedisPoolConfig poolConfig, final String host,
			final int port, final int timeout) {
		this(poolConfig, host, port, timeout, null);
	}

	private static class BinaryJedisFactory extends BasePoolableObjectFactory {
		private final String host;
		private final int port;
		private final int timeout;
		private final String password;

		public BinaryJedisFactory(final String host, final int port, final int timeout,
				final String password) {
			super();
			this.host = host;
			this.port = port;
			this.timeout = (timeout > 0) ? timeout : -1;
			this.password = password;
		}

		public Object makeObject() throws Exception {
			final BinaryJedis BinaryJedis;
			if (timeout > 0) {
				BinaryJedis = new BinaryJedis(this.host, this.port, this.timeout);
			} else {
				BinaryJedis = new BinaryJedis(this.host, this.port);
			}

			BinaryJedis.connect();
			if (null != this.password) {
				BinaryJedis.auth(this.password);
			}
			return BinaryJedis;
		}

		public void destroyObject(final Object obj) throws Exception {
			if (obj instanceof BinaryJedis) {
				final BinaryJedis BinaryJedis = (BinaryJedis) obj;
				if (BinaryJedis.isConnected()) {
					try {
						try {
							BinaryJedis.quit();
						} catch (Exception e) {
						}
						BinaryJedis.disconnect();
					} catch (Exception e) {

					}
				}
			}
		}

		public boolean validateObject(final Object obj) {
			if (obj instanceof BinaryJedis) {
				final BinaryJedis BinaryJedis = (BinaryJedis) obj;
				try {
					return BinaryJedis.isConnected() && BinaryJedis.ping().equals("PONG");
				} catch (final Exception e) {
					return false;
				}
			} else {
				return false;
			}
		}

	}
}
