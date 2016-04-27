package org.bigmouth.nvwa.dpl.jmx;

import org.bigmouth.nvwa.dpl.service.ServiceStatistics;
import org.bigmouth.nvwa.jmx.model.GenericModelMBean;


public class ServiceStatisticsMBean extends GenericModelMBean<ServiceStatistics> {

	public ServiceStatisticsMBean(ServiceStatistics source) {
		super(source);
	}

	@Override
	protected boolean isOperation(String methodName, Class<?>[] paramTypes) {
		return false;
	}
}
