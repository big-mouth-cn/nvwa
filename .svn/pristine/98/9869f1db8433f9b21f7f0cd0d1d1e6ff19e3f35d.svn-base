package org.bigmouth.nvwa.access.response.standard.stream.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;
import org.bigmouth.nvwa.access.response.standard.stream.HttpResponseSegment;
import org.bigmouth.nvwa.access.response.standard.stream.HttpResponseStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpResponseStreamWriteFilter extends IoFilterAdapter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HttpResponseStreamWriteFilter.class);
	private static final AttributeKey WRITE_LOCK = new AttributeKey(
			HttpResponseStreamWriteFilter.class, "standard.stream.write.lock");

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest)
			throws Exception {
		if (!writeRequest.isEncoded()) {
			super.filterWrite(nextFilter, session, writeRequest);
			return;
		}

		if (session.isClosing()) {
			return;
		}

		WriteRequest parentWriteRequest = writeRequest.getOriginalRequest();
		Object message = parentWriteRequest.getMessage();

		if (message instanceof HttpResponseStream) {
			HttpResponseStream response = (HttpResponseStream) message;

			for (;;) {
				if (null == session.setAttributeIfAbsent(WRITE_LOCK, new Object())) {
					IoBuffer encodedHeader = response.getHeader().getContent();
					if (LOGGER.isDebugEnabled())
						LOGGER.debug("HttpResponseStreamWriteFilter.filterWrite,header size:"
								+ (encodedHeader.limit() - encodedHeader.position()));
					nextFilter.filterWrite(session, new DefaultWriteRequest(encodedHeader, null));

					for (HttpResponseSegment seg : response.getBodySegments()) {
						if (session.isClosing()) {
							session.removeAttribute(WRITE_LOCK);
							return;
						}
						IoBuffer encodedBodySeg = seg.getContent();
						if (LOGGER.isDebugEnabled())
							LOGGER.debug("HttpResponseStreamWriteFilter.filterWrite,body segment size:"
									+ (encodedBodySeg.limit() - encodedBodySeg.position()));
						nextFilter.filterWrite(session, new DefaultWriteRequest(encodedBodySeg,
								null));
					}
					session.removeAttribute(WRITE_LOCK);
					return;
				}
			}
		} else {
			super.filterWrite(nextFilter, session, writeRequest);
		}
	}
}
