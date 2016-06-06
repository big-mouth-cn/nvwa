package org.bigmouth.nvwa.transport.jmx;

import org.bigmouth.nvwa.jmx.model.GenericModelMBean;
import org.bigmouth.nvwa.transport.MinaSender;


public class MinaSenderQueueMBean extends GenericModelMBean<MinaSender> {

	public MinaSenderQueueMBean(MinaSender source) {
		super(source);
	}

	@Override
	protected boolean isAttribute(String attrName, Class<?> attrType) {
		if ("pendingSendMessages".equals(attrName))
			return true;
		return false;
	}

	@Override
	protected boolean isOperation(String methodName, Class<?>[] paramTypes) {
		return false;
	}
}
