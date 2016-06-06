package org.bigmouth.nvwa.log.rdb.jmx;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.log.rdb.RecordController;
import org.bigmouth.nvwa.log.rdb.RecordControllerFactory;


public final class MBeanRegisterRecordControllerFactory implements RecordControllerFactory {

	private final LogMBeanExport mbeanExport;
	private final RecordControllerFactory impl;
	private final String plugInName;

	public MBeanRegisterRecordControllerFactory(LogMBeanExport mbeanExport,
			RecordControllerFactory impl, String plugInName) {
		super();
		if (null == mbeanExport)
			throw new NullPointerException("mbeanExport");
		if (null == impl)
			throw new NullPointerException("impl");
		if (StringUtils.isBlank("plugInName"))
			throw new IllegalArgumentException("plugInName is blank.");
		this.mbeanExport = mbeanExport;
		this.impl = impl;
		this.plugInName = plugInName;
	}

	@Override
	public RecordController create(Object logInfo) {
		RecordController ret = impl.create(logInfo);
		mbeanExport.registerRecordControllerMBean(new RecordControllerMBean(ret), plugInName,
				logInfo);
		return ret;
	}
}
