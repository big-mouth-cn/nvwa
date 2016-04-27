package org.bigmouth.nvwa.contentstore.redis.jedis;

import redis.clients.jedis.BinaryJedis;
import redis.clients.util.Pool;

public interface BinaryClientPoolFactory<T extends BinaryJedis> {

	Pool<T> create();
}
