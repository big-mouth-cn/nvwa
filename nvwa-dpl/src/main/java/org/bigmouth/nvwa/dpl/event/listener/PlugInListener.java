package org.bigmouth.nvwa.dpl.event.listener;

import org.bigmouth.nvwa.dpl.event.ActivedEvent;
import org.bigmouth.nvwa.dpl.event.ConfigChangedEvent;
import org.bigmouth.nvwa.dpl.event.CreatedEvent;
import org.bigmouth.nvwa.dpl.event.DeActivedEvent;
import org.bigmouth.nvwa.dpl.event.DestroyedEvent;
import org.bigmouth.nvwa.dpl.event.StatusChangedEvent;

public interface PlugInListener extends LifeCycleListener {

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
