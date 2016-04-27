package org.bigmouth.nvwa.access.filter;

import org.apache.asyncweb.common.HttpRequest;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.bigmouth.nvwa.access.utils.HttpUtils;


public class BlacklistIoFilter extends IoFilterAdapter {

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message)
			throws Exception {
		if(!HttpRequest.class.isAssignableFrom(message.getClass())){
			throw new RuntimeException("Expect HttpRequest,but " + message.getClass());
		}
		HttpRequest httpRequest = (HttpRequest) message;
//		this.uri = HttpUtils.removeReqUriPrefix(request.getRequestUri().toASCIIString().trim());
//		httpRequest.getRequestUri()
	}
}
