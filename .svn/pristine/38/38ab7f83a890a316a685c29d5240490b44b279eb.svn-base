package org.bigmouth.nvwa.dpl.event.listener;

import java.util.EventListener;

import org.bigmouth.nvwa.dpl.event.ActivedEvent;
import org.bigmouth.nvwa.dpl.event.ConfigChangedEvent;
import org.bigmouth.nvwa.dpl.event.CreatedEvent;
import org.bigmouth.nvwa.dpl.event.DeActivedEvent;
import org.bigmouth.nvwa.dpl.event.DestroyedEvent;
import org.bigmouth.nvwa.dpl.event.StatusChangedEvent;


public interface LifeCycleListener extends EventListener {

	void init();

	void destroy();

	void onCreated(CreatedEvent event);

	void onDestroyed(DestroyedEvent event);

	void onActived(ActivedEvent event);

	void onDeActived(DeActivedEvent event);

	void onStatusChanged(StatusChangedEvent event);

	void onConfigChanged(ConfigChangedEvent event);
}
