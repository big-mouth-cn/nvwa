package org.bigmouth.nvwa.servicelogic.statistics;

import org.bigmouth.nvwa.jmx.model.GenericModelMBean;

public class ExtendedServiceStatisticsMBean extends GenericModelMBean<ExtendedServiceStatistics> {

	public ExtendedServiceStatisticsMBean(ExtendedServiceStatistics source) {
		super(source);
	}

	@Override
	protected boolean isOperation(String methodName, Class<?>[] paramTypes) {
		return false;
	}
}
