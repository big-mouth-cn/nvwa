package org.bigmouth.nvwa.access.statistics;

import java.net.InetSocketAddress;
import java.util.Date;

import org.apache.asyncweb.common.HttpRequest;
import org.apache.asyncweb.common.HttpResponse;
import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.bigmouth.nvwa.access.utils.HttpUtils;
import org.bigmouth.nvwa.utils.HexUtils;


class HttpInvocation {

	private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss SSS";
	private static final String SPLIT_ENTER = "\r\n";
	private static final int REQ_CONTENT_TOP_LEN = 256;

	private final IoSession session;
	private final HttpRequest request;
	private final HttpResponse response;
	private final long consumingTime;
	private final String uri;

	HttpInvocation(IoSession session, HttpRequest request, HttpResponse response, long consumingTime) {
		this.session = session;
		this.request = request;
		this.response = response;
		this.consumingTime = consumingTime;
		this.uri = HttpUtils.removeReqUriPrefix(request.getRequestUri().toASCIIString().trim());
	}

	boolean isSuccessInvoke() {
		return HttpResponseStatus.OK == response.getStatus();
	}

	static HttpInvocation create(IoSession session, HttpRequest request, HttpResponse response,
			long consumingTime) {
		return new HttpInvocation(session, request, response, consumingTime);
	}

	IoSession getSession() {
		return session;
	}

	HttpRequest getRequest() {
		return request;
	}

	HttpResponse getResponse() {
		return response;
	}

	long getConsumingTime() {
		return consumingTime;
	}

	String getUri() {
		return uri;
	}

	@Override
	public String toString() {
		String happenTime = DateFormatUtils.format(new Date(), DATE_FORMAT);

		InetSocketAddress isa = (InetSocketAddress) session.getRemoteAddress();
		String peerIp = isa.getAddress().getHostAddress();

		String responseStatusCode = response.getStatus().toString();

		String headers = request.getHeaders().toString();

		IoBuffer buf = request.getContent();
		buf.flip();
		byte[] topContent = new byte[buf.limit() > REQ_CONTENT_TOP_LEN ? REQ_CONTENT_TOP_LEN : buf
				.limit()];
		buf.get(topContent);
		String content = HexUtils.bytesAsHexCodes(topContent, REQ_CONTENT_TOP_LEN);

		return new StringBuilder(512).append("----------------------------").append(SPLIT_ENTER)
				.append("happenTime:").append(happenTime).append(SPLIT_ENTER).append("peerIp:")
				.append(peerIp).append(SPLIT_ENTER).append("consumingTime:").append(consumingTime)
				.append(SPLIT_ENTER).append("responseStatusCode:").append(responseStatusCode)
				.append(SPLIT_ENTER).append("uri:").append(uri).append(SPLIT_ENTER)
				.append("headers:").append(headers).append(SPLIT_ENTER).append("content:")
				.append(content).append(SPLIT_ENTER).toString();
	}
}
