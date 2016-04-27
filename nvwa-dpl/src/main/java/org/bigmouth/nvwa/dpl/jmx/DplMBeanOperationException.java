package org.bigmouth.nvwa.dpl.jmx;

import org.bigmouth.nvwa.jmx.MBeanOperationException;

public class DplMBeanOperationException extends MBeanOperationException {

	private static final long serialVersionUID = 4237147680186734924L;

	public DplMBeanOperationException() {
		super();
	}

	public DplMBeanOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DplMBeanOperationException(String message) {
		super(message);
	}

	public DplMBeanOperationException(Throwable cause) {
		super(cause);
	}
}
