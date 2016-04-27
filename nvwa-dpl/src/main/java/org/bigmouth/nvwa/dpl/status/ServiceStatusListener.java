package org.bigmouth.nvwa.dpl.status;

import org.bigmouth.nvwa.dpl.event.AfterExecutionFiredEvent;
import org.bigmouth.nvwa.dpl.event.BeforeExecutionFiredEvent;
import org.bigmouth.nvwa.dpl.event.ExecutionFailedEvent;
import org.bigmouth.nvwa.dpl.event.listener.ServiceListener;

public class ServiceStatusListener extends StatusListener implements ServiceListener {

	public ServiceStatusListener(StatusHolder statusHolder) {
		super(statusHolder);
	}

	@Override
	public void afterExecute(AfterExecutionFiredEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeExecute(BeforeExecutionFiredEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onExecuteFailed(ExecutionFailedEvent event) {
		// TODO Auto-generated method stub
	}
}
