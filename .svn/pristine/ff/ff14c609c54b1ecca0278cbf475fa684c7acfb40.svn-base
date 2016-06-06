package org.bigmouth.nvwa.dpl.event;

public class ExecutionFailedEvent extends ExecutionEvent {

	private static final long serialVersionUID = 1L;

	private final Throwable cause;

	public ExecutionFailedEvent(Object source, Throwable cause, Object... executeStatus) {
		super(source, executeStatus);
		this.cause = cause;
	}

	public Throwable getCause() {
		return cause;
	}
}
