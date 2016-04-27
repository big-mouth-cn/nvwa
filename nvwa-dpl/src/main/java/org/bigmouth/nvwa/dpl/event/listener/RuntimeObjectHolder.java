package org.bigmouth.nvwa.dpl.event.listener;

import org.bigmouth.nvwa.dpl.service.Service;

public final class RuntimeObjectHolder {

	private static ThreadLocal<Object> RO_INVOCATION = new ThreadLocal<Object>();
	private static ThreadLocal<Service> RO_SERVICE = new ThreadLocal<Service>();

	private RuntimeObjectHolder() {
	}

	public static Service getService() {
		return RO_SERVICE.get();
	}

	public static Object getInvocation() {
		return RO_INVOCATION.get();
	}

	static void setInvocation(Object invocation) {
		if (null != getInvocation())
			return;
		if (null == invocation)
			throw new NullPointerException("invocation");
		RO_INVOCATION.set(invocation);
	}

	static void setService(Service service) {
		if (null != getService())
			return;
		if (null == service)
			throw new NullPointerException("service");
		RO_SERVICE.set(service);
	}

	static void removeInvocation() {
		RO_INVOCATION.remove();
	}

	static void removeService() {
		RO_SERVICE.remove();
	}

	static void removeAll() {
		removeInvocation();
		removeService();
	}
}
