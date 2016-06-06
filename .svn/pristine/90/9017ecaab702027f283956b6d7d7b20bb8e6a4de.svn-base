package org.bigmouth.nvwa.access.response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.UUID;

import org.apache.asyncweb.common.HttpResponse;

import com.google.common.collect.Maps;

public class ProxyHttpResponseExtFactory implements HttpResponseExtFactory {

	private final HttpResponseFactory httpResponseFactory;

	public ProxyHttpResponseExtFactory(HttpResponseFactory httpResponseFactory) {
		if (null == httpResponseFactory)
			throw new NullPointerException("httpResponseFactory");
		this.httpResponseFactory = httpResponseFactory;
	}

	@Override
	public HttpResponseExt create(HttpResponseSource httpResponseSource, UUID tid) {
		HttpResponse httpResponse = httpResponseFactory.create(httpResponseSource);
		return (HttpResponseExt) Proxy.newProxyInstance(httpResponse.getClass().getClassLoader(),
				new Class<?>[] { HttpResponseExt.class }, new InnerInvocationHandler(
						(HttpResponse) httpResponse, tid));
	}

	private static final class InnerInvocationHandler implements InvocationHandler {
		private final HttpResponse httpResponse;
		private final UUID transactionId;
		private final Map<String, Object> valueCache = Maps.newHashMap();

		private InnerInvocationHandler(HttpResponse httpResponse, UUID tid) {
			this.httpResponse = httpResponse;
			this.transactionId = tid;

			valueCache.put("getIdentification", transactionId);
			valueCache.put("existIdentification", true);
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			Object v = valueCache.get(methodName);
			if (null != v)
				return v;
			return method.invoke(httpResponse, args);
		}
	}
}
