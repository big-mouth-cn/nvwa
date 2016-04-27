package org.bigmouth.nvwa.access.request;

import java.util.Collections;
import java.util.List;

import org.apache.asyncweb.common.HttpMethod;
import org.apache.asyncweb.common.HttpRequest;

import com.google.common.collect.Lists;

public class UnsupportHttpMethodException extends IllegalAccessProtocolException {

	private static final long serialVersionUID = 1023144166579907077L;

	private final List<HttpMethod> expectMethods = Lists.newArrayList();

	public UnsupportHttpMethodException(HttpRequest httpRequest, Throwable e,
			List<HttpMethod> expectMethods) {
		super(httpRequest, e);
		if (null != expectMethods && expectMethods.size() > 0) {
			this.expectMethods.addAll(expectMethods);
		}
	}

	public UnsupportHttpMethodException(HttpRequest httpRequest, String message,
			List<HttpMethod> expectMethods) {
		super(httpRequest, message);
		if (null != expectMethods && expectMethods.size() > 0) {
			this.expectMethods.addAll(expectMethods);
		}
	}

	public List<HttpMethod> getExpectMethods() {
		return Collections.unmodifiableList(expectMethods);
	}

	public HttpMethod getIllegalMethod() {
		return getIllegalRequest().getMethod();
	}
}
