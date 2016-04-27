package org.bigmouth.nvwa.dpl.event.listener;

import org.bigmouth.nvwa.dpl.event.ActivedEvent;
import org.bigmouth.nvwa.dpl.event.AfterExecutionFiredEvent;
import org.bigmouth.nvwa.dpl.event.BeforeExecutionFiredEvent;
import org.bigmouth.nvwa.dpl.event.ConfigChangedEvent;
import org.bigmouth.nvwa.dpl.event.CreatedEvent;
import org.bigmouth.nvwa.dpl.event.DeActivedEvent;
import org.bigmouth.nvwa.dpl.event.DestroyedEvent;
import org.bigmouth.nvwa.dpl.event.ExecutionFailedEvent;
import org.bigmouth.nvwa.dpl.event.StatusChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ServiceListenerAdapter implements ServiceListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceListenerAdapter.class);

	@Override
	public void init() {
		// Empty Handler
	}

	@Override
	public void destroy() {
		// Empty Handler
	}

	@Override
	public void afterExecute(AfterExecutionFiredEvent event) {
		// Empty Handler
	}

	@Override
	public void beforeExecute(BeforeExecutionFiredEvent event) {
		// Empty Handler
	}

	@Override
	public void onActived(ActivedEvent event) {
		// Empty Handler
	}

	@Override
	public void onConfigChanged(ConfigChangedEvent event) {
		// Empty Handler
	}

	@Override
	public void onCreated(CreatedEvent event) {
		// Empty Handler
	}

	@Override
	public void onDeActived(DeActivedEvent event) {
		// Empty Handler
	}

	@Override
	public void onDestroyed(DestroyedEvent event) {
		// Empty Handler
	}

	@Override
	public void onStatusChanged(StatusChangedEvent event) {
		// Empty Handler
	}

	@Override
	public void onExecuteFailed(ExecutionFailedEvent event) {
		if (LOGGER.isWarnEnabled()) {
			LOGGER.warn("[Service]" + event.getService().getName()
					+ " EXCEPTION, please implement " + getClass().getName()
					+ ".exceptionCaught() for proper handling:", event.getCause());
		}
	}
}
