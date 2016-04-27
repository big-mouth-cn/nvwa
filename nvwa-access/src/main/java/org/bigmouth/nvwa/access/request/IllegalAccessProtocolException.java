package org.bigmouth.nvwa.access.request;

import org.apache.asyncweb.common.HttpRequest;

public class IllegalAccessProtocolException extends RuntimeException {

	private static final long serialVersionUID = 6692236408170830654L;

	private final HttpRequest httpRequest;

	public IllegalAccessProtocolException(HttpRequest httpRequest, Throwable e) {
		super(e);
		if (null == httpRequest)
			throw new IllegalArgumentException("httpRequest is null.");
		this.httpRequest = httpRequest;
	}

	public IllegalAccessProtocolException(HttpRequest httpRequest, String message) {
		super(message);
		if (null == httpRequest)
			throw new IllegalArgumentException("httpRequest is null.");
		this.httpRequest = httpRequest;
	}

	public HttpRequest getIllegalRequest() {
		return this.httpRequest;
	}

	public String getIllegalRequestDesc() {
		return httpRequest.toString();
	}
}
