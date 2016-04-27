package org.bigmouth.nvwa.distributed;


import org.bigmouth.nvwa.distributed.jmx.EndpointRegistrarMBean;
import org.bigmouth.nvwa.distributed.notify.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class EndpointRegistrar implements EndpointRegistrarMBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(EndpointRegistrar.class);

	private static final String DEFAULT_CONNECTOR = "_";

	private final Notifier notifier;
	private final String namespace;
	private final Endpoint endpoint;
	private final String connector;

	public EndpointRegistrar(Notifier notifier, String namespace, String host, int port, int weights) {
		this(notifier, namespace, host, port, weights, DEFAULT_CONNECTOR);
	}

	public EndpointRegistrar(Notifier notifier, String namespace, String host, int port,
			int weights, String connector) {
		this.notifier = notifier;
		this.namespace = namespace;
		this.endpoint = Endpoint.of(host, port, weights);
		this.connector = connector;
	}

	public void register() {
		String path = this.endpoint.getEndpointPath(getNamespace(), connector);
		getNotifier().notifyPathAdded(path,
				String.valueOf(this.getEndpoint().getWeights()).getBytes());
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("ServerRegistrar register[{}] successful.", path);
	}

	private Endpoint getEndpoint() {
		return this.endpoint;
	}

	private Notifier getNotifier() {
		return notifier;
	}

	private String getNamespace() {
		return namespace;
	}

	@Override
	public void setWeights(int weights) {
		Preconditions.checkArgument(weights >= 0 && weights <= 9, "weights:" + weights);
		this.getEndpoint().setWeights(weights);
	}

	@Override
	public String getDesc() {
		return this.getEndpoint().toString();
	}

	@Override
	public int getWeights() {
		return this.getEndpoint().getWeights();
	}
}
