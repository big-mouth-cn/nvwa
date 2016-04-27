package org.bigmouth.nvwa.servicelogic;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bigmouth.nvwa.dpl.PlugInServiceBus;
import org.bigmouth.nvwa.transport.Replier;

public class InvocationContext {

	private final PlugInServiceBus dplBus;
	private final Replier replier;
	private final ConcurrentMap<String, Object> contextMap = new ConcurrentHashMap<String, Object>();

	// private final TransactionInvocation???

	public InvocationContext(PlugInServiceBus dplBus, Replier replier) {
		this.dplBus = dplBus;
		this.replier = replier;
	}

	public Replier getReplier() {
		return replier;
	}

	public PlugInServiceBus getDplBus() {
		return dplBus;
	}

	public Map<String, Object> getContextMap() {
		return Collections.unmodifiableMap(contextMap);
	}

	public void setContextMap(Map<String, Object> properties) {
		contextMap.putAll(properties);
	}

	public void put(String key, Object value) {
		contextMap.put(key, value);
	}

	public Object get(String key) {
		return contextMap.get(key);
	}
}
