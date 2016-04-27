package org.bigmouth.nvwa.transport;

import java.net.InetSocketAddress;

import org.apache.commons.lang.StringUtils;
import org.apache.mina.core.service.IoConnector;

public final class DefaultTargetSessionLocatorBuilder implements TargetSessionLocatorBuilder {

	private static final int DEFAULT_MAX_SESSION_COUNT = 3;
	private static final long DEFAULT_RECONNECT_TIMEOUT = 10 * 1000;

	private final int maxSessionCount;
	private final long reconnectTimeout;

	public DefaultTargetSessionLocatorBuilder() {
		this(DEFAULT_MAX_SESSION_COUNT, DEFAULT_RECONNECT_TIMEOUT);
	}

	public DefaultTargetSessionLocatorBuilder(int maxSessionCount, long reconnectTimeout) {
		super();
		this.maxSessionCount = maxSessionCount;
		this.reconnectTimeout = reconnectTimeout;
	}

	@Override
	public TargetSessionLocator build(IoConnector connector, InetSocketAddress endPointAddress,
			String threadName) {
		if (null == connector)
			throw new NullPointerException("connector");
		if (null == endPointAddress)
			throw new NullPointerException("endPointAddress");
		if (StringUtils.isBlank(threadName))
			throw new IllegalArgumentException("threadName");
		DefaultTargetSessionLocator sessionLocator = new DefaultTargetSessionLocator();
		TargetSessionHolder sessionHolder = new TargetSessionHolder(connector, endPointAddress,
				maxSessionCount, reconnectTimeout, sessionLocator, threadName);
		sessionHolder.start();
		sessionLocator.setSessionHolder(sessionHolder);

		return sessionLocator;
	}
}
