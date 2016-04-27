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

public interface ServiceListener extends LifeCycleListener {

	void beforeExecute(BeforeExecutionFiredEvent event);

	/**
	 * only service executed <b>successfully</b>,trigger this event.
	 * 
	 * @param event
	 */
	void afterExecute(AfterExecutionFiredEvent event);

	/**
	 * only service executed <b>failed</b>,trigger this event.
	 * 
	 * @param event
	 */
	void onExecuteFailed(ExecutionFailedEvent event);

	@Override
	void init();

	@Override
	void destroy();

	@Override
	void onCreated(CreatedEvent event);

	@Override
	void onDestroyed(DestroyedEvent event);

	@Override
	void onActived(ActivedEvent event);

	@Override
	void onDeActived(DeActivedEvent event);

	@Override
	void onStatusChanged(StatusChangedEvent event);

	@Override
	void onConfigChanged(ConfigChangedEvent event);
}
