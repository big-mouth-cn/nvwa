package org.bigmouth.nvwa.contentstore.impl;

import java.util.List;

import org.bigmouth.nvwa.contentstore.ContentStore;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;


/**
 * 
 * @author 王喆 ContentStore包装类，提供本地缓存功能
 * 
 */
public class LocalCachedContentStore implements ContentStore {
	// 缓存
	private Cache cache;

	private ContentStore contentStore;

	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public void setContentStore(ContentStore contentStore) {
		this.contentStore = contentStore;
	}

	/**
	 * 先查询缓存中是否存在 在查询ContentStore
	 */
	public boolean contains(String key) {
		// 先判断缓存中是否存在
		if (cache.isKeyInCache(key)) {
			return true;
		}
		return contentStore.contains(key);
	}

	/**
	 * 先从缓存中提取，没有取到从CS读取，只有当数据不为空时才将数据放入Cache
	 */
	public byte[] fetch(String key) {
		if (key == null)
			return null;
		Element element = cache.get(key);
		if (element != null) {
			return (byte[]) element.getObjectValue();
		}
		byte[] data = contentStore.fetch(key);
		if (data != null)
			cache.put(new Element(key, data));
		return data;
	}

	// 移除key
	public boolean remove(String key) {
		cache.remove(key);
		return contentStore.remove(key);
	}

	public String store(byte[] content) {
		if (content == null || content.length == 0)
			return null;
		String key = contentStore.store(content);
		cache.put(new Element(key, content));
		return key;
	}

	public List<String> getKeys() {
		return contentStore.getKeys();
	}

}
