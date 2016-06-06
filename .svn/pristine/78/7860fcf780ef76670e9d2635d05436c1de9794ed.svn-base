package org.bigmouth.nvwa.access.response;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.asyncweb.common.DefaultHttpResponse;
import org.apache.asyncweb.common.HttpHeaderConstants;
import org.apache.asyncweb.common.HttpResponse;
import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.asyncweb.common.MutableHttpResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.access.utils.HttpUtils;


public class DefaultHttpResponseFactory implements HttpResponseFactory {

	@SuppressWarnings("unused")
	private static final String HEADER_KEY_CONTENT_RANGE = "Content-Range";
	private static final String HEADER_KEY_DATE = "Date";
	private static final String HEADER_KEY_SKYID = "SkyId";
	private static final String HEADER_VALUE_SKYID = "SkyMarket";
	private static final String HEADER_VALUE_CONTENT_TYPE = "application/octet-stream";

	@Override
	public HttpResponse create(HttpResponseSource httpResponseSource) {
		HttpResponseStatus status = httpResponseSource.getStatus();
		Map<String, String> headers = httpResponseSource.getHeaders();
		String contentType = httpResponseSource.getContentType();
		IoBuffer content = httpResponseSource.getContent();

		if (null == status)
			throw new NullPointerException("status");
		if (null == content)
			content = IoBuffer.allocate(0);
		if (!content.hasRemaining())
			content.flip();

		MutableHttpResponse httpResponse = createHttpResponse();
		setStatus(status, httpResponse);
		setHeaders(headers, contentType, content, httpResponse);
		setContent(content, httpResponse);

		return httpResponse;
	}

	protected MutableHttpResponse createHttpResponse() {
		MutableHttpResponse httpResponse = new DefaultHttpResponse();
		return httpResponse;
	}

	protected void setStatus(HttpResponseStatus status, MutableHttpResponse httpResponse) {
		httpResponse.setStatus(status);
	}

	protected void setHeaders(Map<String, String> headers, String contentType, IoBuffer content,
			MutableHttpResponse httpResponse) {
		if (null != headers) {
			for (Entry<String, String> e : headers.entrySet()) {
				String headerName = e.getKey();
				String headerValue = e.getValue();
				if (StringUtils.isBlank(headerName) || StringUtils.isBlank(headerValue))
					continue;
				httpResponse.addHeader(headerName, headerValue);
			}
		}

		httpResponse.setHeader(HttpHeaderConstants.KEY_CONTENT_LENGTH,
				String.valueOf(content.limit()));

		if (StringUtils.isBlank(contentType))
			contentType = HEADER_VALUE_CONTENT_TYPE;
		httpResponse.setContentType(contentType);

		httpResponse.setHeader(HEADER_KEY_DATE, HttpUtils.genGMTString());
		httpResponse.setHeader(HEADER_KEY_SKYID, HEADER_VALUE_SKYID);
	}

	protected void setContent(IoBuffer content, MutableHttpResponse httpResponse) {
		httpResponse.setContent(content);
	}
}
