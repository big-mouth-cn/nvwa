package org.bigmouth.nvwa.dpl.jmx;

import java.util.Iterator;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanException;
import javax.management.ReflectionException;

import org.bigmouth.nvwa.dpl.event.listener.ReviseContextClassLoaderServiceListener;
import org.bigmouth.nvwa.dpl.event.listener.ServiceListener;
import org.bigmouth.nvwa.dpl.service.Service;
import org.bigmouth.nvwa.jmx.model.GenericModelMBean;


public class ServiceMBean extends GenericModelMBean<Service> {

	public ServiceMBean(Service source) {
		super(source);
	}

	@Override
	public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException,
			ReflectionException {
		if ("allEventListener".equals(attribute)) {
			Service service = getSource();
			StringBuilder sb = new StringBuilder(64);
			for (Iterator<ServiceListener> it = service.getAllEventListener(); it.hasNext();) {
				ServiceListener listener = it.next();
				if (listener instanceof ReviseContextClassLoaderServiceListener) {
					listener = ((ReviseContextClassLoaderServiceListener) listener).getListener();
				}
				sb.append(listener).append(",");
			}
			if (sb.length() > 0) {
				return sb.substring(0, sb.length() - 1);
			} else {
				return "";
			}
		} else {
			return super.getAttribute(attribute);
		}
	}

	@Override
	protected boolean isAttribute(String attrName, Class<?> attrType) {
		if (attrName.matches("(config|plugInConfig|status)"))
			return false;
		return super.isAttribute(attrName, attrType);
	}

	@Override
	protected boolean isOperation(String methodName, Class<?>[] paramTypes) {
		if (methodName.matches("(init|destroy|clearEventListeners)"))
			return false;
		return super.isOperation(methodName, paramTypes);
	}
}
