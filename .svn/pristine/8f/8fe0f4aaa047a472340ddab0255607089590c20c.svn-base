package org.bigmouth.nvwa.contentstore.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.bigmouth.nvwa.contentstore.ContentStore;


/**
 * only for test.
 * 
 * @author nada
 * 
 */
public class DefaultContentStore implements ContentStore {

	private static final Logger logger = Logger.getLogger(DefaultContentStore.class);

	@Override
	public boolean contains(String key) {
		return true;
	}

	@Override
	public byte[] fetch(String key) {
		logger.info("invoke mock(fetch),return new byte[]().");
		return new byte[0];
	}

	@Override
	public List<String> getKeys() {
		return null;
	}

	@Override
	public boolean remove(String key) {
		logger.info("invoke mock(remove),return true.");
		return true;
	}

	@Override
	public String store(byte[] content) {
		logger.info("invoke mock(store),return 'key'.");
		return "key";
	}

}
