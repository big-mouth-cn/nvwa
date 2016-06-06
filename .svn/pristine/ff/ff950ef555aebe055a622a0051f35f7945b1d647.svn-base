package org.bigmouth.nvwa.contentstore.redis;

import java.util.List;

public interface RedisContentStore {

	boolean remove(String key);

	boolean contains(String key);

	byte[] fetch(String key);

	void store(String key, byte[] value);

	void store(String key, List<?> value);

	String store(byte[] value);

	String store(List<?> value);

	List<byte[]> listRange(String key, int begin, int end);

	long listLength(String key);

	/**
	 * Invoke appendListTail.
	 * 
	 * @param key
	 * @param value
	 */
	@Deprecated
	void appendList(String key, byte[] value);

	void appendListHead(String key, byte[] value);

	void appendListTail(String key, byte[] value);

	void removeItemFromListTail(String key, byte[] value, int matchCount);

	void removeItemFromListHead(String key, byte[] value, int matchCount);

	// removeItemFromListTail
	// removeItemFromListHead

	long incrementAndGet(String key, long step);

	long decrementAndGet(String key, long step);

	long get(String key);

	long set(String key, long value);

	void expire(String key, int timeout);
}
