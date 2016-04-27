package org.bigmouth.nvwa.utils.jmx.server;

import java.net.MalformedURLException;

import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.StringUtils;

public abstract class JmxmpMBeanServer extends AbstractMBeanServer implements SimpleMBeanServer {

	private static final String PROTOCOL = "jmxmp";

	@Override
	protected JMXServiceURL createJMXServiceURL() throws MalformedURLException {
		String hostName = getHostName();
		if (StringUtils.isBlank(hostName))
			throw new IllegalArgumentException("hostName");
		int port = getPort();
		return new JMXServiceURL(PROTOCOL, hostName, port);
	}
}
