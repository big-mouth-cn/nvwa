package org.bigmouth.nvwa.access.service;

import java.util.Map;

import org.bigmouth.nvwa.sap.ExtendedItem;
import org.bigmouth.nvwa.sap.ExtendedItemType;

import com.google.common.collect.Maps;

public class ContentExtractorSelector implements ContentExtractor {

	private final Map<ExtendedItemType, ContentExtractor> extractors = Maps.newHashMap();

	public ContentExtractorSelector(Map<ExtendedItemType, ContentExtractor> transformers) {
		this.extractors.putAll(transformers);
	}

	@Override
	public byte[] extract(ExtendedItem extendedItem) throws ContentNotFoundException {
		if (null == extendedItem)
			throw new NullPointerException("extendedItem");

		ContentExtractor extractor = extractors.get(extendedItem.getType());
		if (null == extractor) {
			// TODO:
			throw new RuntimeException("Can not found any contentExtractor for type:"
					+ extendedItem.getType());
		}
		return extractor.extract(extendedItem);
	}
}
