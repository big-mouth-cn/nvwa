package org.bigmouth.nvwa.cache.xmc;

import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.bigmouth.nvwa.cache.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XmcUpdateService implements UpdateService {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmcUpdateService.class);

	private MemcachedClient memcachedClient;

	@Override
	public void update(String key, Object value, int exp) {
		try {
			memcachedClient.set(key, exp, value);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("key[{}] updated from memcached successful.", key);
		} catch (TimeoutException e) {
			throw new XmcMemcachedException("memcachedClient.set:", e);
		} catch (InterruptedException e) {
			throw new XmcMemcachedException("memcachedClient.set:", e);
		} catch (MemcachedException e) {
			throw new XmcMemcachedException("memcachedClient.set:", e);
		}
	}

	@Override
	public void update(String key, Object value) {
		update(key, value, 0);
	}

	@Override
	public void remove(String key) {
		boolean ret = false;
		try {
			ret = memcachedClient.delete(key);
		} catch (TimeoutException e) {
			throw new XmcMemcachedException("memcachedClient.delete:", e);
		} catch (InterruptedException e) {
			throw new XmcMemcachedException("memcachedClient.delete:", e);
		} catch (MemcachedException e) {
			throw new XmcMemcachedException("memcachedClient.delete:", e);
		}
		
		if(ret){
			LOGGER.debug("key[{}] deleted from memcahced successful.", key);
		}else{
			LOGGER.debug("key[{}] deleted from memcahced fail.", key);
		}
//		
//		if (!ret) {
//			throw new XmcMemcachedException("memcachedClient.delete:return false.");
//		} else if (LOGGER.isDebugEnabled()) {
//			LOGGER.debug("key[{}] deleted from memcahced successful.", key);
//		}
	}

	@Override
	public void removeAll() {
		try {
			memcachedClient.flushAll();
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("memcached flushAll successful.");
		} catch (TimeoutException e) {
			throw new XmcMemcachedException("memcachedClient.flushAll:", e);
		} catch (InterruptedException e) {
			throw new XmcMemcachedException("memcachedClient.flushAll:", e);
		} catch (MemcachedException e) {
			throw new XmcMemcachedException("memcachedClient.flushAll:", e);
		}
	}

	@Override
	public Object getOriginalClient() {
		return memcachedClient;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}
}
