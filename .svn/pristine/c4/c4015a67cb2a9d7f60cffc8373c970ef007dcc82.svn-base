package org.bigmouth.nvwa.dpl.utils;

import org.bigmouth.nvwa.dpl.event.listener.RuntimeObjectHolder;
import org.bigmouth.nvwa.dpl.service.Service;
import org.bigmouth.nvwa.dpl.service.ServiceStatistics;

public final class RuntimeUtils {

	private RuntimeUtils() {
	}

	public static Service getService() {
		Service service = RuntimeObjectHolder.getService();
		if (null == service)
			throw new IllegalStateException("Not in service execution cycle.");
		return service;
	}

	public static ServiceStatistics getServiceStatistics() {
		return getService().getStatistics();
	}

	public static Object getServiceAttachment() {
		return getService().getAttachment();
	}

	public static Object getInvocation() {
		Object iv = RuntimeObjectHolder.getInvocation();
		if (null == iv)
			throw new IllegalStateException("Not in service execution cycle.");
		return iv;
	}
}
