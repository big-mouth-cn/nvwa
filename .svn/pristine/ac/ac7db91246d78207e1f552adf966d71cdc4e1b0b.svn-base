package org.bigmouth.nvwa.access.response.standard.stream;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.asyncweb.common.Cookie;
import org.apache.asyncweb.common.HttpResponse;
import org.apache.asyncweb.common.HttpResponseStatus;
import org.apache.asyncweb.common.HttpVersion;
import org.apache.mina.core.buffer.IoBuffer;
import org.bigmouth.nvwa.sap.Identifiable;

import com.google.common.collect.Lists;

public class HttpResponseStream implements HttpResponse, Identifiable {

	private final HttpResponseHeader header;
	private final UUID tid;
	private final List<HttpResponseSegment> bodySegments = Lists.newArrayList();

	public HttpResponseStream(HttpResponseHeader header, UUID tid) {
		this.header = header;
		this.tid = tid;
	}

	public HttpResponseHeader getHeader() {
		return this.header;
	}

	public void addBodySegment(HttpResponseSegment segment) {
		this.bodySegments.add(segment);
	}

	public void addBodySegments(List<HttpResponseSegment> bodySegments) {
		this.bodySegments.addAll(bodySegments);
	}

	public List<HttpResponseSegment> getBodySegments() {
		return bodySegments;
	}

	@Override
	public UUID getIdentification() {
		return tid;
	}

	@Override
	public boolean existIdentification() {
		return null != tid;
	}

	@Override
	public HttpVersion getProtocolVersion() {
		return header.getEntity().getProtocolVersion();
	}

	@Override
	public String getContentType() {
		return header.getEntity().getContentType();
	}

	@Override
	public boolean isKeepAlive() {
		return header.getEntity().isKeepAlive();
	}

	@Override
	public String getHeader(String name) {
		return header.getEntity().getHeader(name);
	}

	@Override
	public boolean containsHeader(String name) {
		return header.getEntity().containsHeader(name);
	}

	@Override
	public Map<String, List<String>> getHeaders() {
		return header.getEntity().getHeaders();
	}

	@Override
	public Set<Cookie> getCookies() {
		return header.getEntity().getCookies();
	}

	@Override
	public IoBuffer getContent() {
		return header.getEntity().getContent();
	}

	@Override
	public HttpResponseStatus getStatus() {
		return header.getEntity().getStatus();
	}

	@Override
	public String getStatusReasonPhrase() {
		return header.getEntity().getStatusReasonPhrase();
	}
}
