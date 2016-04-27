package org.bigmouth.nvwa.dpl.event;

import org.bigmouth.nvwa.dpl.service.Service;

public class ExecutionEvent extends LifeCycleEvent {

	private static final long serialVersionUID = 1L;

	private final Object[] invocation;

	public ExecutionEvent(Object source, Object... executeStatus) {
		super(source);
		if (!(source instanceof Service))
			throw new IllegalArgumentException("source expect Service,but " + source.getClass());
		if (null == executeStatus)
			executeStatus = new Object[0];
		this.invocation = executeStatus;
	}

	public Service getService() {
		return (Service) getSource();
	}

	public Object[] getInvocation() {
		return invocation;
	}
}
