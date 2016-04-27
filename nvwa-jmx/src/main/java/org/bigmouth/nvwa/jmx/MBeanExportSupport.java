package org.bigmouth.nvwa.jmx;

import java.lang.management.ManagementFactory;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MBeanExportSupport {

	private static final Logger LOGGER = LoggerFactory.getLogger(MBeanExportSupport.class);

	private static final MBeanServer DEFAULT_MBEAN_SERVER = ManagementFactory
			.getPlatformMBeanServer();

	private final Set<ObjectName> registeredBeans = new LinkedHashSet<ObjectName>();
	private final MBeanServer server;

	public MBeanExportSupport(MBeanServer mbeanServer) {
		if (null == mbeanServer)
			throw new NullPointerException("mbeanServer");
		this.server = mbeanServer;
	}

	public MBeanExportSupport() {
		this(DEFAULT_MBEAN_SERVER);
	}

	public void register(Object mbean, ObjectName objectName) {
		if (null == mbean)
			throw new NullPointerException("mbean");
		if (null == objectName)
			throw new NullPointerException("objectName");
		if (isRegistered(objectName))
			return;
		try {
			this.server.registerMBean(mbean, objectName);
		} catch (InstanceAlreadyExistsException e) {
			throw new MBeanOperationException("register:", e);
		} catch (MBeanRegistrationException e) {
			throw new MBeanOperationException("register:", e);
		} catch (NotCompliantMBeanException e) {
			throw new MBeanOperationException("register:", e);
		}
		registeredBeans.add(objectName);

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("MBean " + objectName + " has bean registered.");
	}

	public void unregister(ObjectName objectName) {
		if (null == objectName)
			throw new NullPointerException("objectName");
		doUnregister(objectName);
		registeredBeans.remove(objectName);

		if (LOGGER.isDebugEnabled())
			LOGGER.debug("MBean " + objectName + " has bean unregister.");
	}

	private void doUnregister(ObjectName objectName) {
		try {
			this.server.unregisterMBean(objectName);
		} catch (MBeanRegistrationException e) {
			throw new MBeanOperationException("unregister:", e);
		} catch (InstanceNotFoundException e) {
			throw new MBeanOperationException("unregister:", e);
		}
	}

	public boolean isRegistered(ObjectName objectName) {
		if (null == objectName)
			throw new NullPointerException("objectName");
		return this.server.isRegistered(objectName);
	}

	public void destroy() {
		for (ObjectName objectName : registeredBeans) {
			doUnregister(objectName);
		}
		registeredBeans.clear();
	}
}
