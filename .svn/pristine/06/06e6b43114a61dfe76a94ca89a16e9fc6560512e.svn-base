package org.bigmouth.nvwa.utils.jmx.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.management.MBeanServer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurableMBeanServer implements SimpleMBeanServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableMBeanServer.class);

	private boolean jmxEnable = true;
	private ConnectProtocol protocol = ConnectProtocol.JMXMP;
	private int controlPort = 7890;
	private String rmiServiceName = "defaultRMIService";

	private boolean started = false;
	private SimpleMBeanServer impl;

	protected boolean isStarted() {
		return started;
	}

	@Override
	public synchronized int getMBeanCount() {
		if (!isStarted())
			throw new IllegalStateException("MBeanServer is not started.");
		return impl.getMBeanCount();
	}

	@Override
	public synchronized MBeanServer getNativeMBeanServer() {
		if (!isStarted())
			throw new IllegalStateException("MBeanServer is not started.");
		return impl.getNativeMBeanServer();
	}

	@Override
	public synchronized boolean isActive() {
		if (!isStarted())
			throw new IllegalStateException("MBeanServer is not started.");
		return impl.isActive();
	}

	@Override
	public synchronized boolean isRegistered(String name) {
		if (!isStarted())
			throw new IllegalStateException("MBeanServer is not started.");
		return impl.isRegistered(name);
	}

	@Override
	public synchronized void registMBean(Object o) {
		if (!isStarted())
			throw new IllegalStateException("MBeanServer is not started.");
		impl.registMBean(o);
	}

	@Override
	public synchronized void registMBean(Object o, String name) {
		if (!isStarted())
			throw new IllegalStateException("MBeanServer is not started.");
		impl.registMBean(o, name);
	}

	@Override
	public synchronized void start() {
		if (started)
			return;
		boolean jmxEnable = jmxEnable();
		if (jmxEnable) {
			// protocol
			ConnectProtocol protocol = getConnectProtocol();
			if (null == protocol)
				throw new NullPointerException("ConnectProtocol");
			// port
			int port = getControlPort();
			if (port <= 0 || port > 65535)
				throw new IllegalArgumentException("port:" + port);

			String connectDesc = null;
			if (protocol == ConnectProtocol.RMI) {// rmi
				String serviceName = getRmiServiceName();
				impl = SimpleMBeanServerUtils.newRmiMBeanServer(port, serviceName);
				connectDesc = "service:jmx:rmi:///jndi/rmi://" + getLocalAddress() + ":" + port
						+ "/" + serviceName;
			} else {// jmxmp
				impl = SimpleMBeanServerUtils.newJmxmpMBeanServer(port);
				connectDesc = "service:jmx:jmxmp://" + getLocalAddress() + ":" + port;
			}

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("MBean Server is started over " + protocol + ",listening on port:"
						+ port + ".\r\nConnect Description:[" + connectDesc + "]");
			}
		} else {
			if (LOGGER.isInfoEnabled())
				LOGGER.info("Proxy MBean Server is !NOT! enable.");
		}
		impl.start();
		started = true;
	}

	private String getLocalAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			throw new RuntimeException("InetAddress.getLocalHost:", e);
		}
	}

	@Override
	public synchronized void stop() {
		if (!started)
			return;
		impl.stop();
		impl = null;
		started = false;
		if (LOGGER.isInfoEnabled())
			LOGGER.info("Proxy MBean Server is stopped.");
	}

	protected boolean jmxEnable() {
		return jmxEnable;
	}

	protected ConnectProtocol getConnectProtocol() {
		return protocol;
	}

	protected int getControlPort() {
		return controlPort;
	}

	protected String getRmiServiceName() {
		return rmiServiceName;
	}

	public void setJmxEnable(boolean jmxEnable) {
		this.jmxEnable = jmxEnable;
	}

	public void setProtocol(ConnectProtocol protocol) {
		if (null == protocol)
			throw new NullPointerException("protocol");
		this.protocol = protocol;
	}

	public void setControlPort(int controlPort) {
		if (controlPort <= 0 || controlPort > 65535)
			throw new IllegalArgumentException("controlPort:" + controlPort);
		this.controlPort = controlPort;
	}

	public void setRmiServiceName(String rmiServiceName) {
		if (StringUtils.isBlank(rmiServiceName))
			throw new IllegalArgumentException("rmiServiceName is blank.");
		this.rmiServiceName = rmiServiceName;
	}
}
