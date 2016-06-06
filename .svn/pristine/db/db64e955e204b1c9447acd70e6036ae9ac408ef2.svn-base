package org.bigmouth.nvwa.utils.jmx.server;

import org.apache.commons.lang.StringUtils;

public final class SimpleMBeanServerUtils {

	private SimpleMBeanServerUtils() {
	}

	public static SimpleMBeanServer newJmxmpMBeanServer(final int port) {
		return newJmxmpMBeanServer("0.0.0.0", port);
	}

	public static SimpleMBeanServer newJmxmpMBeanServer(final String hostName, final int port) {
		if (StringUtils.isBlank(hostName))
			throw new IllegalArgumentException("hostName");
		return new JmxmpMBeanServer() {

			@Override
			protected String getHostName() {
				return hostName;
			}

			@Override
			protected int getPort() {
				return port;
			}
		};
	}

	public static SimpleMBeanServer newRmiMBeanServer(final int port, final String serviceName) {
		return newRmiMBeanServer(port, serviceName);
	}

	public static SimpleMBeanServer newRmiMBeanServer(final String hostName, final int port,
			final String serviceName) {
		if (StringUtils.isBlank(hostName))
			throw new IllegalArgumentException("hostName");
		if (StringUtils.isBlank(serviceName))
			throw new IllegalArgumentException("serviceName");
		return new RmiMBeanServer() {

			@Override
			protected String getHostName() {
				return hostName;
			}

			@Override
			protected int getPort() {
				return port;
			}

			@Override
			protected String getServiceName() {
				return serviceName;
			}
		};
	}
}
