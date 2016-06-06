package org.bigmouth.nvwa.access;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.commons.io.IOUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.access.request.HttpRequestExt;
import org.bigmouth.nvwa.access.request.IllegalRequestHandler;
import org.bigmouth.nvwa.access.response.HttpResponseExtFactory;
import org.bigmouth.nvwa.access.response.HttpResponseSource;
import org.bigmouth.nvwa.access.utils.HttpUtils;
import org.bigmouth.nvwa.sap.SapRequest;
import org.bigmouth.nvwa.transport.Replier;
import org.bigmouth.nvwa.transport.Sender;
import org.bigmouth.nvwa.utils.Transformer;


public class ChinaCacheHttpReactor extends HttpReactor {

	public ChinaCacheHttpReactor(
			Transformer<HttpRequestExt, SapRequest> http2SapRequestTransformer, Sender sender,
			Replier replier, HttpResponseExtFactory httpResponseExtFactory,
			IllegalRequestHandler illegalRequestHandler) {
		super(http2SapRequestTransformer, sender, replier, httpResponseExtFactory,
				illegalRequestHandler);
	}

	public ChinaCacheHttpReactor(
			Transformer<HttpRequestExt, SapRequest> http2SapRequestTransformer, Sender sender,
			Replier replier, HttpResponseExtFactory httpResponseExtFactory) {
		super(http2SapRequestTransformer, sender, replier, httpResponseExtFactory);
	}

	private static final String CHINA_CACHE_CHECK_URL = "/do_not_delete/noc.gif";

	@Override
	protected boolean filteRequest(HttpRequestExt httpRequest) {
		String reqUri = HttpUtils.removeReqUriPrefix(httpRequest.getRequestUri().toASCIIString());
		if (CHINA_CACHE_CHECK_URL.equals(reqUri.trim())) {
			InputStream is = ChinaCacheHttpReactor.class.getResourceAsStream(CHINA_CACHE_CHECK_URL);
			byte[] content = null;
			try {
				content = IOUtils.toByteArray(is);
			} catch (IOException e) {
				throw new RuntimeException("filteRequest:", e);
			}
			final IoBuffer contentBuf = IoBuffer.wrap(content);
			reply(new HttpResponseSource() {

				@Override
				public HttpResponseStatus getStatus() {
					return HttpResponseStatus.OK;
				}

				@Override
				public Map<String, String> getHeaders() {
					return null;
				}

				@Override
				public String getContentType() {
					return "image/gif";
				}

				@Override
				public IoBuffer getContent() {
					return contentBuf;
				}
			}, httpRequest.getIdentification());

			return false;
		}
		return true;
	}
}
