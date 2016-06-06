package org.bigmouth.nvwa.utils.jmx.server;

import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.StringUtils;

public abstract class RmiMBeanServer extends AbstractMBeanServer implements SimpleMBeanServer {

	@Override
	protected JMXServiceURL createJMXServiceURL() throws MalformedURLException {
		String hostName = getHostName();
		if (StringUtils.isBlank(hostName))
			throw new IllegalArgumentException("hostName is blank.");
		int port = getPort();
		String serviceName = getServiceName();
		if (StringUtils.isBlank(serviceName))
			throw new IllegalArgumentException("serviceName is blank.");
		try {
			initRegistry(port);
		} catch (AccessException e) {
			throw new RuntimeException("createJMXServiceURL:", e);
		} catch (RemoteException e) {
			throw new RuntimeException("createJMXServiceURL:", e);
		}
		String serverURL = "service:jmx:rmi:///jndi/rmi://" + hostName + ":" + port + "/"
				+ serviceName;
		return new JMXServiceURL(serverURL);
	}

	private void initRegistry(int port) throws RemoteException, AccessException {
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(port);
			registry.list();
		} catch (Exception e) {
			registry = null;
		}
		if (null == registry)
			registry = LocateRegistry.createRegistry(port);

		registry.list();
	}

	protected abstract String getServiceName();
}
