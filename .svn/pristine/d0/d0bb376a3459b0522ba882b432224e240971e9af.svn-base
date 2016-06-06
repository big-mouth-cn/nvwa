package org.bigmouth.nvwa.transport;

import org.apache.asyncweb.common.HttpHeaderConstants;
import org.apache.asyncweb.common.HttpRequest;
import org.apache.asyncweb.common.HttpResponse;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.bigmouth.nvwa.sap.Identifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpMinaReplier extends MinaReplier {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpMinaReplier.class);

	public HttpMinaReplier() {
		super();
	}

	public HttpMinaReplier(boolean multiTransaction) {
		super(multiTransaction);
	}

	@Override
	protected void replyCompleted(Identifiable request, Identifiable response,
			boolean completeClose, IoSession session, WriteFuture wf) {
		HttpRequest httpRequest = getHttpRequest(request);
		HttpResponse httpResponse = getHttpResponse(response);

		// no content
		String contentLength = httpResponse.getHeader(HttpHeaderConstants.KEY_CONTENT_LENGTH);
		if (null == contentLength || "0".equals(contentLength)) {
			wf.addListener(IoFutureListener.CLOSE);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Http response !NOT! contains Content-Length,Httpserver closing session when transport completed.");
			return;
		}

		// !NOT! keep alive
		if (!httpRequest.isKeepAlive()) {
			wf.addListener(IoFutureListener.CLOSE);
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Http response is !NOT! KeepAlive,Httpserver closing session when transport completed.");
			return;
		}

		// super.replyCompleted(request, response, completeClose, session, wf);
	}

	private HttpResponse getHttpResponse(Identifiable response) {
		if (null == response)
			throw new NullPointerException("response");
		if (!(response instanceof HttpResponse))
			throw new IllegalArgumentException("request expect HttpResponse,but "
					+ response.getClass());
		return (HttpResponse) response;
	}

	private HttpRequest getHttpRequest(Identifiable request) {
		if (null == request)
			throw new NullPointerException("request");
		if (!(request instanceof HttpRequest))
			throw new IllegalArgumentException("request expect HttpRequest,but "
					+ request.getClass());
		return (HttpRequest) request;
	}
}
