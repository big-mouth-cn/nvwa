package org.bigmouth.nvwa.access.service.sharding;

import org.bigmouth.nvwa.contentstore.ContentStore;

public class DefaultContentStoreLocator implements ContentStoreLocator {

	private final ContentStore contentStore;

	public DefaultContentStoreLocator(ContentStore contentStore) {
		super();
		if (null == contentStore)
			throw new NullPointerException("contentStore");
		this.contentStore = contentStore;
	}

	@Override
	public ContentStore lookup(int contentFlag) throws ContentStoreNotFoundException {
		return contentStore;
	}
}
