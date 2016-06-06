package org.bigmouth.nvwa.access.response;

import java.util.Map;
import java.util.UUID;

import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.access.service.ContentExtractor;
import org.bigmouth.nvwa.access.service.ContentNotFoundException;
import org.bigmouth.nvwa.sap.ContentType;
import org.bigmouth.nvwa.sap.ExtendedItem;
import org.bigmouth.nvwa.sap.Identifiable;
import org.bigmouth.nvwa.sap.SapResponse;
import org.bigmouth.nvwa.utils.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class Sap2HttpResponseTransformer implements Transformer<SapResponse, Identifiable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Sap2HttpResponseTransformer.class);

	private final HttpResponseExtFactory httpResponseExtFactory;
	private final ContentExtractor contentExtractor;

	public Sap2HttpResponseTransformer(HttpResponseExtFactory httpResponseExtFactory,
			ContentExtractor contentExtractor) {
		this.httpResponseExtFactory = httpResponseExtFactory;
		this.contentExtractor = contentExtractor;
	}

	@Override
	public Identifiable transform(SapResponse input) {
		try {
			IoBuffer contentBuf = mergeResponseContent(input);
			HttpResponseStatus httpRespStatus = HttpResponseStatus.forId(input.getStatus()
					.getCode());
			ContentType contentType = input.getContentType();
			HttpResponseSource httpResponseSource = createHttpResponseSource(httpRespStatus,
					contentType, contentBuf);
			return createHttpResponseExt(httpResponseSource, input.getIdentification());
		} catch (Exception e) {
			LOGGER.error("transform:", e);
			return createHttpResponseExt(new HttpResponseSourceImpl(
					HttpResponseStatus.INTERNAL_SERVER_ERROR, input.getContentType(), null),
					input.getIdentification());
		}
	}

	protected HttpResponseSource createHttpResponseSource(HttpResponseStatus httpRespStatus,
			ContentType contentType, IoBuffer content) {
		return new HttpResponseSourceImpl(httpRespStatus, contentType, content);
	}

	private IoBuffer mergeResponseContent(SapResponse input) {
		IoBuffer contentBuf = null;
		if (input.existsExtendedItems()) {
			contentBuf = IoBuffer.allocate(input.getContentLength() * 2);
			contentBuf.setAutoExpand(true);

			IoBuffer bodyContent = input.getContent();
			bodyContent.flip();

			for (ExtendedItem item : input.getExtendedItems()) {
				byte[] staticContent = null;
				try {
					staticContent = contentExtractor.extract(item);
				} catch (ContentNotFoundException e) {
					LOGGER.error("contentExtractor.extract:", e);
					throw new RuntimeException("contentExtractor.extract:", e);
				}

				if (null == staticContent || 0 == staticContent.length)
					continue;

				if (bodyContent.position() != item.getMergeOffset()) {
					int segLimit = item.getMergeOffset();
					if (-1 == segLimit) {// append end of body content.
						segLimit = input.getContentLength();
					}
					bodyContent.limit(segLimit);
					IoBuffer seg = bodyContent.slice();
					contentBuf.put(seg);

					bodyContent.position(segLimit);
				}
				contentBuf.put(staticContent);
			}
			if (bodyContent.capacity() > bodyContent.limit()) {
				bodyContent.limit(bodyContent.capacity());
				IoBuffer lastSeg = bodyContent.slice();
				contentBuf.put(lastSeg);
			}

			contentBuf.flip();
		} else {
			contentBuf = input.getContent();
		}
		return contentBuf;
	}

	private HttpResponseExt createHttpResponseExt(HttpResponseSource httpResponseSource, UUID tid) {
		return httpResponseExtFactory.create(httpResponseSource, tid);
	}

	protected static final class HttpResponseSourceImpl implements HttpResponseSource {

		private HttpResponseStatus status;
		private final ContentType contentType;
		private final IoBuffer content;
		private final Map<String, String> headers = Maps.newHashMap();

		public HttpResponseSourceImpl(HttpResponseStatus status, ContentType contentType,
				IoBuffer content) {
			this.status = status;
			this.contentType = contentType;
			this.content = content;
		}

		public void setStatus(HttpResponseStatus status) {
			this.status = status;
		}

		@Override
		public IoBuffer getContent() {
			return content;
		}

		@Override
		public String getContentType() {
			return contentType.httpHeader();
		}

		public void addHeader(String key, String value) {
			this.headers.put(key, value);
		}

		@Override
		public Map<String, String> getHeaders() {
			return headers;
		}

		@Override
		public HttpResponseStatus getStatus() {
			return status;
		}
	}
}
