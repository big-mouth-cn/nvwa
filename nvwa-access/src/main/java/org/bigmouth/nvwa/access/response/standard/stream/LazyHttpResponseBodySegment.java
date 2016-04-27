package org.bigmouth.nvwa.access.response.standard.stream;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.access.service.ContentNotFoundException;
import org.bigmouth.nvwa.contentstore.ContentStore;

import com.google.common.collect.Lists;

public class LazyHttpResponseBodySegment implements HttpResponseSegment {

	private static final int DEFAULT_SEG_LEN = 16 * 1024;
	private static final int DEFAULT_POSITION = -1;
	private static final int DEFAULT_LIMIT = -1;

	private final ContentStore contentStore;
	private final List<String> contentFlags = Lists.newArrayList();
	private int position = DEFAULT_POSITION;
	private int limit = DEFAULT_LIMIT;

	public LazyHttpResponseBodySegment(ContentStore contentStore) {
		super();
		if (null == contentStore)
			throw new NullPointerException("contentStore");
		this.contentStore = contentStore;
	}

	public LazyHttpResponseBodySegment setPosition(int position) {
		this.position = position;
		return this;
	}

	public LazyHttpResponseBodySegment setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public LazyHttpResponseBodySegment addContentFlag(String flag) {
		if (StringUtils.isBlank("flag"))
			throw new IllegalArgumentException("flag is blank.");
		this.contentFlags.add(flag);
		return this;
	}

	@Override
	public IoBuffer getContent() throws ContentNotFoundException {
		IoBuffer ret = IoBuffer.allocate(DEFAULT_SEG_LEN * contentFlags.size());
		ret.setAutoExpand(true);
		for (String flag : contentFlags) {
			byte[] seg = contentStore.fetch(flag);
			if (null == seg || 0 == seg.length)
				throw new ContentNotFoundException("ContentFlag:" + flag);
			ret.put(seg);
		}

		ret.flip();
		if (DEFAULT_POSITION == position && DEFAULT_LIMIT == limit) {
			return ret;
		} else {
			if (DEFAULT_POSITION != position) {
				ret.position(position);
			}
			if (DEFAULT_LIMIT != limit) {
				ret.limit(limit);
			}
		}

		return ret.slice();
	}
}
