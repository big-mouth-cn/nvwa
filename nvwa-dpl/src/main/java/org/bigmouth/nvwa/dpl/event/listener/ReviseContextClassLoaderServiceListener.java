package org.bigmouth.nvwa.dpl.event.listener;

import org.bigmouth.nvwa.dpl.ClassLoaderHolder;
import org.bigmouth.nvwa.dpl.event.AfterExecutionFiredEvent;
import org.bigmouth.nvwa.dpl.event.BeforeExecutionFiredEvent;
import org.bigmouth.nvwa.dpl.event.ExecutionFailedEvent;

public class ReviseContextClassLoaderServiceListener extends
		ReviseContextClassLoaderListener<ServiceListener> implements ServiceListener {

	public ReviseContextClassLoaderServiceListener(ClassLoaderHolder classLoaderHolder,
			ServiceListener listener) {
		super(classLoaderHolder, listener);
	}

	@Override
	public void beforeExecute(BeforeExecutionFiredEvent event) {
		getListener().beforeExecute(event);
	}

	@Override
	public void afterExecute(AfterExecutionFiredEvent event) {
		getListener().afterExecute(event);
	}

	@Override
	public void onExecuteFailed(ExecutionFailedEvent event) {
		getListener().onExecuteFailed(event);
	}
}
