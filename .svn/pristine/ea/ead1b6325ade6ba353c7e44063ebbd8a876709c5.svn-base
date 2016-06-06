package org.bigmouth.nvwa.access.response.standard.stream;

import org.apache.mina.core.buffer.IoBuffer;

public class HttpResponseBodySegment implements HttpResponseSegment {

	private final IoBuffer content;

	public HttpResponseBodySegment(IoBuffer content) {
		super();
		if (null == content)
			throw new NullPointerException("content");
		if (!content.hasRemaining())
			throw new IllegalArgumentException("content ");
		this.content = content;
	}

	@Override
	public IoBuffer getContent() {
		return content;
	}
}
