package org.bigmouth.nvwa.access.statistics;

import org.apache.asyncweb.common.HttpRequest;
import org.apache.asyncweb.common.HttpResponse;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.bigmouth.nvwa.access.response.standard.stream.codec.HttpResponseStreamWriteFilter;


public class StatisticsIoFilter extends IoFilterAdapter {

	private static final AttributeKey REQ_BEGIN_TIME = new AttributeKey(
			HttpResponseStreamWriteFilter.class, "access.req.begin.time");
	private static final AttributeKey REQ_OBJ = new AttributeKey(
			HttpResponseStreamWriteFilter.class, "access.req.obj");

	private final AccessStatistics statistics;

	public StatisticsIoFilter(AccessStatistics statistics) {
		if (null == statistics)
			throw new NullPointerException("statistics");
		this.statistics = statistics;
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session, Object message)
			throws Exception {
		session.setAttribute(REQ_BEGIN_TIME, System.currentTimeMillis());
		session.setAttribute(REQ_OBJ, message);

		nextFilter.messageReceived(session, message);
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest)
			throws Exception {
		HttpResponse response = (HttpResponse) writeRequest.getMessage();
		HttpRequest request = (HttpRequest) session.getAttribute(REQ_OBJ);
		long consumingTime = System.currentTimeMillis()
				- (Long) session.getAttribute(REQ_BEGIN_TIME);

		HttpInvocation invocation = HttpInvocation
				.create(session, request, response, consumingTime);

		if (invocation.isSuccessInvoke()) {
			statistics.increaseSuccessTask(invocation);
		} else {
			statistics.increaseFailTask(invocation);
		}

		nextFilter.messageSent(session, writeRequest);
	}
}
