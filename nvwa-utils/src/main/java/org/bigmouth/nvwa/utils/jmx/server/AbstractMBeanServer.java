package org.bigmouth.nvwa.utils.jmx.server;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMBeanServer implements SimpleMBeanServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMBeanServer.class);

	private MBeanServer mbserver = null;
	private JMXConnectorServer connectorServer = null;
	private final Object lock = new Object();
	private volatile boolean started = false;

	@Override
	public void start() {
		synchronized (lock) {
			if (isStarted())
				return;
			try {
				mbserver = createMBeanServer();
				if (null == mbserver)
					throw new NullPointerException("mbserver");
				connectorServer = createConnectorServer();
				if (null == connectorServer)
					throw new NullPointerException("connectorServer");
				startConnectorServer();
				started = true;
			} catch (Exception e) {
				LOGGER.error("create MBServer error", e);
			}
			mbserver = createMBeanServer();
		}
	}

	private boolean isStarted() {
		return started;
	}

	@Override
	public void stop() {
		synchronized (lock) {
			if (!started)
				return;
			try {
				connectorServer.stop();
			} catch (IOException e) {
				LOGGER.error("destroy:", e);
			}
			connectorServer = null;
			mbserver = null;
		}
	}

	private void startConnectorServer() throws IOException {
		connectorServer.start();
	}

	protected MBeanServer createMBeanServer() {
		// mbserver = MBeanServerFactory.newMBeanServer();
		return ManagementFactory.getPlatformMBeanServer();
	}

	protected abstract JMXServiceURL createJMXServiceURL() throws MalformedURLException;

	protected JMXConnectorServer createConnectorServer() throws MalformedURLException, IOException {
		JMXServiceURL serviceURL = createJMXServiceURL();
		return JMXConnectorServerFactory.newJMXConnectorServer(serviceURL, null, mbserver);
	}

	protected String getHostName() {
		return "0.0.0.0";
	}

	protected abstract int getPort();

	private void check() {
		if (!isStarted())
			throw new RuntimeException("MBeanServer is not started.");
	}

	@Override
	public boolean isRegistered(String name) {
		check();
		try {
			return mbserver != null && mbserver.isRegistered(new ObjectName(name));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isActive() {
		return mbserver != null && connectorServer != null && connectorServer.isActive();
	}

	@Override
	public int getMBeanCount() {
		check();
		if (mbserver != null) {
			return mbserver.getMBeanCount();
		} else {
			return 0;
		}
	}

	@Override
	public void registMBean(Object o) {
		check();
		if (null == o)
			throw new NullPointerException();
		String name = o.getClass().getPackage().getName() + ":type=" + o.getClass().getSimpleName();
		registMBean(o, name);
	}

	@Override
	public void registMBean(Object o, String name) {
		check();
		if (isRegistered(name))
			return;
		if (mbserver != null) {
			try {
				mbserver.registerMBean(o, new ObjectName(name));
			} catch (Exception e) {
				LOGGER.error("registMBean:", e);
			}
		}
	}

	@Override
	public MBeanServer getNativeMBeanServer() {
		check();
		return mbserver;
	}
}
