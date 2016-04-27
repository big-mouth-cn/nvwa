package org.bigmouth.nvwa.servicelogic.codec;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bigmouth.nvwa.sap.ContentType;
import org.bigmouth.nvwa.servicelogic.codec.annotation.CodecType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class DefaultCodecSelector implements CodecSelector {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCodecSelector.class);

	/*----------------Class 2 ContentType-----------------*/
	private final ConcurrentMap<Class<?>, SoftReference<ContentType>> class2ContentTypes = new ConcurrentHashMap<Class<?>, SoftReference<ContentType>>();

	/*----------------ContentType 2 Codec-----------------*/
	private final Map<ContentType, ContentEncoder> contentType2Encoders = Maps.newHashMap();
	private final Map<ContentType, ContentDecoder> contentType2Decoders = Maps.newHashMap();

	public DefaultCodecSelector(Map<ContentType, ContentEncoder> contentType2Encoders,
			Map<ContentType, ContentDecoder> contentType2Decoders) {
		if (null == contentType2Encoders || 0 == contentType2Encoders.size())
			throw new NullPointerException("contentType2Encoders");
		if (null == contentType2Decoders || 0 == contentType2Decoders.size())
			throw new NullPointerException("contentType2Decoders");
		this.contentType2Encoders.putAll(contentType2Encoders);
		this.contentType2Decoders.putAll(contentType2Decoders);
	}

	@Override
	public ContentEncoder selectEncoder(Class<?> template) {
		if (null == template)
			throw new NullPointerException("template");
		ContentType contentType = selectContentType(template);
		ContentEncoder ret = contentType2Encoders.get(contentType);
		if (null == ret)
			throw new RuntimeException("Not support encoding for[" + contentType
					+ "] response yet,template:" + template);
		return ret;
	}

	@Override
	public ContentDecoder selectDecoder(Class<?> template) {
		if (null == template)
			throw new NullPointerException("template");
		ContentType contentType = selectContentType(template);
		return contentType2Decoders.get(contentType);
	}

	@Override
	public ContentType selectContentType(Class<?> template) {
		SoftReference<ContentType> sr = class2ContentTypes.get(template);
		ContentType contentType = null;
		if (null == sr || null == (contentType = sr.get())) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Get ContentType from reflection.");
			}
			CodecType codecType = template.getAnnotation(CodecType.class);
			contentType = null == codecType ? ContentType.TLV : codecType.value();
			class2ContentTypes.put(template, new SoftReference<ContentType>(contentType));
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Get ContentType from cache.");
			}
		}
		return contentType;
	}
}
