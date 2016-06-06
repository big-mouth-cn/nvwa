package org.bigmouth.nvwa.log.rdb.jmx;

import org.bigmouth.nvwa.jmx.model.GenericModelMBean;
import org.bigmouth.nvwa.log.rdb.RecordController;


public final class RecordControllerMBean extends GenericModelMBean<RecordController> {

	public RecordControllerMBean(RecordController source) {
		super(source);
	}

	@Override
	protected boolean isOperation(String methodName, Class<?>[] paramTypes) {
		return false;
	}

	@Override
	protected boolean isAttribute(String attrName, Class<?> attrType) {
		if (attrName.matches("(pendingMessages|recordSql)"))
			return true;
		return false;
	}
}
