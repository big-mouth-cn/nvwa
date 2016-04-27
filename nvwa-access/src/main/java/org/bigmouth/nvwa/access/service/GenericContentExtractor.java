package org.bigmouth.nvwa.access.service;

import java.io.ByteArrayOutputStream;

import org.bigmouth.nvwa.access.service.sharding.ContentStoreLocator;
import org.bigmouth.nvwa.access.service.sharding.ContentStoreNotFoundException;
import org.bigmouth.nvwa.contentstore.ContentStore;
import org.bigmouth.nvwa.sap.ExtendedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GenericContentExtractor implements ContentExtractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericContentExtractor.class);

	private final ContentStoreLocator contentStoreLocator;

	public GenericContentExtractor(ContentStoreLocator contentStoreLocator) {
		if (null == contentStoreLocator)
			throw new NullPointerException("contentStoreLocator");
		this.contentStoreLocator = contentStoreLocator;
	}

	@Override
	public byte[] extract(ExtendedItem extendedItem) throws ContentNotFoundException {
		if (null == extendedItem)
			throw new NullPointerException("extendedItem");
		if (0 == extendedItem.getContentFlagsCount()) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("0 == extendedItem.getContentFlagsCount(),ignore.");
			return new byte[0];
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (String cf : extendedItem.getContentFlags()) {
			ContentStore contentStore = getContentStore(extendedItem);
			byte[] bytes = contentStore.fetch(cf);
			if (null == bytes || 0 == bytes.length)
				throw new ContentNotFoundException("ExtendedItem.type[" + extendedItem.getType()
						+ "] ContentFlag[" + cf + "] can not found content.");
			out.write(bytes, 0, bytes.length);
		}

		if (null == out || 0 == out.size()) {
			// TODO:just ignore
			return new byte[0];
		}
		byte[] content = out.toByteArray();
		content = extendedItem.getExactContent(content);
		if (extendedItem.isRequiredWrap()) {
			content = extendedItem.wrapContent(content);
		}

		return content;
	}

	private ContentStore getContentStore(ExtendedItem extendedItem) throws ContentNotFoundException {
		int shardingMark = extendedItem.getShardingMark();
		ContentStore contentStore = null;
		try {
			contentStore = contentStoreLocator.lookup(shardingMark);
		} catch (ContentStoreNotFoundException e) {
			throw new ContentNotFoundException(e);
		}
		return contentStore;
	}
}
