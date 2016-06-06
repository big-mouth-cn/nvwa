package org.bigmouth.nvwa.dpl.event.listener;

import org.bigmouth.nvwa.dpl.event.AfterExecutionFiredEvent;
import org.bigmouth.nvwa.dpl.event.BeforeExecutionFiredEvent;
import org.bigmouth.nvwa.dpl.event.ExecutionFailedEvent;

public class RuntimeObjectServiceListener extends ServiceListenerAdapter {

	@Override
	public void beforeExecute(BeforeExecutionFiredEvent event) {
		RuntimeObjectHolder.setService(event.getService());
		RuntimeObjectHolder.setInvocation(event.getInvocation()[0]);
	}

	@Override
	public void afterExecute(AfterExecutionFiredEvent event) {
		RuntimeObjectHolder.removeAll();
	}

	@Override
	public void onExecuteFailed(ExecutionFailedEvent event) {
		RuntimeObjectHolder.removeAll();
	}
}
