package org.bigmouth.nvwa.cluster;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.bigmouth.nvwa.cluster.node.Updatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class ServiceLogicNodeHolder implements
		Updatable<Map<InetSocketAddress, InetSocketAddress>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLogicNodeHolder.class);

	private int accessPort;
	private final AtomicReference<Map<InetSocketAddress, InetSocketAddress>> mappingNodes = new AtomicReference<Map<InetSocketAddress, InetSocketAddress>>();

	public InetSocketAddress getServiceLogicNode() {
		String accessIp = null;
		try {
			accessIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			throw new RuntimeException("getServiceLogicNode:", e);
		}

		InetSocketAddress accessNode = new InetSocketAddress(accessIp, accessPort);
		InetSocketAddress servicelogicNode = this.mappingNodes.get().get(accessNode);

		if (null == servicelogicNode) {
			if (LOGGER.isErrorEnabled())
				LOGGER.error("Can not found servicelogic node for access node:[" + accessNode
						+ "].");
		}

		return servicelogicNode;
	}

	@Override
	public void update(Map<InetSocketAddress, InetSocketAddress> data) {
		this.mappingNodes.set(data);
	}

	public void setAccessPort(int accessPort) {
		this.accessPort = accessPort;
	}
}
