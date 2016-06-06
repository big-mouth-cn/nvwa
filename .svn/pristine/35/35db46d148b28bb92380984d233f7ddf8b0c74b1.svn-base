package org.bigmouth.nvwa.cache.local;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.bigmouth.nvwa.cache.DataExtractor;
import org.bigmouth.nvwa.cache.FetchService;
import org.bigmouth.nvwa.cache.KeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EhCacheFetchService implements FetchService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EhCacheFetchService.class);

	private final Cache cache;

	public EhCacheFetchService(Cache cache) {
		super();
		this.cache = cache;
	}

	@Override
	public <T> T fetch(KeyGenerator keyGenerator, DataExtractor dataExtractor, Class<T> clazz) {
		return fetch(keyGenerator, dataExtractor, clazz, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T fetch(KeyGenerator keyGenerator, DataExtractor dataExtractor, Class<T> clazz,
			int exp) {
		if (null == keyGenerator)
			throw new NullPointerException("keyGenerator");

		String key = keyGenerator.generateKey();
		Object fromCache = null;
		Element element = cache.get(key);
		if (element != null) {
			fromCache = (T) element.getObjectValue();
		}

		if (null != fromCache) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("get data from ehcache for key[" + key + "]");
			return (T) fromCache;
		}

		return (T) getFromStore(dataExtractor, key, exp);
	}

	private Object getFromStore(DataExtractor dataExtractor, String key, int exp) {
		Object fromStore = dataExtractor.extract();
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("get data from dataExtractor for key [" + key + "],!NOT! from ehcache.");

		if (null != fromStore) {
			Element element = new Element(key, fromStore);
			cache.put(element);
		}
		return fromStore;
	}

	@Override
	public Object getOriginalClient() {
		return cache;
	}
}
