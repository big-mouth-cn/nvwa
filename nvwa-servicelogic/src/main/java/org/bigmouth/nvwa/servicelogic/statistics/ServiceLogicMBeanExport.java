package org.bigmouth.nvwa.servicelogic.statistics;

import javax.management.ObjectName;

import org.bigmouth.nvwa.dpl.jmx.DplMBeanExport;
import org.bigmouth.nvwa.dpl.jmx.DplMBeanOperationException;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.bigmouth.nvwa.dpl.service.Service;

public class ServiceLogicMBeanExport extends DplMBeanExport {

	private static final String DPL_MBEAN_DOMAIN = "0.dynamic.plugin.library";

	@Override
	public void registerServiceMBean(PlugIn plugIn, Service service) {
		super.registerServiceMBean(plugIn, service);

		Object attachment = service.getAttachment();
		if (null == attachment)
			return;

		try {
			ExtendedServiceStatisticsMBean extendedServiceStatisticsMBean = new ExtendedServiceStatisticsMBean(
					(ExtendedServiceStatistics) attachment);

			ObjectName extendedServiceStatisticsObjectName = new ObjectName(DPL_MBEAN_DOMAIN
					+ ":type=" + plugIn.getKey() + "_statistics_extended,name=serivice_"
					+ service.getKey());
			register(extendedServiceStatisticsMBean, extendedServiceStatisticsObjectName);
		} catch (Exception e) {
			throw new DplMBeanOperationException("extendedServiceStatisticsMBean:", e);
		}
	}
}
