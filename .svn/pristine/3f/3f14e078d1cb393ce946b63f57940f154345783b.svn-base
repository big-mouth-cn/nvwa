package org.bigmouth.nvwa.access.request;

import org.apache.asyncweb.common.HttpRequest;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;

public class HttpRequestExtFilter extends IoFilterAdapter {

	private final HttpRequestExtFactory httpRequestExtFactory;

	public HttpRequestExtFilter(HttpRequestExtFactory httpRequestExtFactory) {
		this.httpRequestExtFactory = httpRequestExtFactory;
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message)
			throws Exception {
		HttpRequest httpRequest = (HttpRequest) message;
		HttpRequestExt httpRequestExt = httpRequestExtFactory.create(httpRequest, session);
		nextFilter.messageReceived(session, httpRequestExt);
	}
}
