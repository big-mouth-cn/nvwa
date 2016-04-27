package org.bigmouth.nvwa.dpl.event;

public class BeforeExecutionFiredEvent extends ExecutionEvent {

	private static final long serialVersionUID = 1L;

	public BeforeExecutionFiredEvent(Object source, Object... executeStatus) {
		super(source, executeStatus);
	}
}
