package org.bigmouth.nvwa.dpl.event;

public class AfterExecutionFiredEvent extends ExecutionEvent {

	private static final long serialVersionUID = 1L;

	public AfterExecutionFiredEvent(Object source, Object... executeStatus) {
		super(source, executeStatus);
	}
}
