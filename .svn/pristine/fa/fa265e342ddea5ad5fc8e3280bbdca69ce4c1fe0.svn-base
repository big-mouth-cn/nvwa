package org.bigmouth.nvwa.access.request;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Map;
import java.util.UUID;

import org.apache.asyncweb.common.HttpRequest;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.bigmouth.nvwa.sap.Ip;
import org.bigmouth.nvwa.sap.IpPort;
import org.bigmouth.nvwa.transport.MinaReplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class ProxyHttpRequestExtFactory implements HttpRequestExtFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyHttpRequestExtFactory.class);
	private static final CharsetDecoder DEFAULT_CHARSET_DECODER = Charset.forName("UTF-8")
			.newDecoder();

	@Override
	public HttpRequestExt create(HttpRequest httpRequest, IoSession session) {
		return (HttpRequestExt) Proxy.newProxyInstance(httpRequest.getClass().getClassLoader(),
				new Class<?>[] { HttpRequestExt.class }, new InnerInvocationHandler(
						(HttpRequest) httpRequest, session));
	}

	// TODO:
	private static final class InnerInvocationHandler implements InvocationHandler {
		private final HttpRequest httpRequest;
		private final Ip clientIp;
		private final IpPort accessAddress;
		private final Map<String, Object> valueCache = Maps.newHashMap();

		private InnerInvocationHandler(HttpRequest httpRequest, IoSession session) {
			this.httpRequest = httpRequest;

			InetSocketAddress peerAddress = (InetSocketAddress) session.getRemoteAddress();
			clientIp = Ip.create(peerAddress.getAddress().getHostAddress());

			InetSocketAddress localAddress = (InetSocketAddress) session.getLocalAddress();
			accessAddress = IpPort.create(localAddress.getAddress().getHostAddress(),
					localAddress.getPort());

			valueCache.put("getIdentification", getTransactionId(session));
			valueCache.put("existIdentification", true);
			valueCache.put("getClientIp", clientIp);
			valueCache.put("getAccessAddress", accessAddress);
			valueCache.put("toString", getHttpRequestDesc());
		}

		private UUID getTransactionId(IoSession session) {
			UUID transactionId = MinaReplier.getTransactionId(session);
			return null == transactionId ? UUID.randomUUID() : transactionId;
		}

		/**
		 * method uri version\key0=value0 key1=value1\content
		 * 
		 * @return
		 */
		private String getHttpRequestDesc() {
			StringBuilder sb = new StringBuilder(2048);
			sb.append(httpRequest.getMethod().name()).append(" ")
					.append(httpRequest.getRequestUri()).append(" ")
					.append(httpRequest.getProtocolVersion().name()).append("\\");
			sb.append(httpRequest.getHeaders());
			IoBuffer contentBuf = httpRequest.getContent().duplicate();

			try {
				String content = contentBuf.getString(DEFAULT_CHARSET_DECODER);
				if (null != content)
					sb.append(content);
			} catch (CharacterCodingException e) {
				LOGGER.debug("getHttpRequestDesc:", e);
			}

			return sb.toString();
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			String methodName = method.getName();
			Object v = valueCache.get(methodName);
			if (null != v)
				return v;
			return method.invoke(httpRequest, args);
		}
	}
}
