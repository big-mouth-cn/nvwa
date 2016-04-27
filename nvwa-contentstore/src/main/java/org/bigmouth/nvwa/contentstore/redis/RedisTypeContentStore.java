package org.bigmouth.nvwa.contentstore.redis;

import java.util.List;

public interface RedisTypeContentStore extends RedisContentStore {

	<T> T fetch(String key, Class<T> clazz);

	void store(String key, Object value);

	String store(Object value);

	<T> List<T> listRange(String key, int begin, int end, Class<T> elementClass);

	/**
	 * Invoke appendListTail.
	 * 
	 * @param key
	 * @param value
	 */
	@Deprecated
	void appendList(String key, Object value);

	void appendListHead(String key, Object value);

	void appendListTail(String key, Object value);
}
