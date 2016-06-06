package org.bigmouth.nvwa.dpl;

import java.util.Iterator;

import org.bigmouth.nvwa.dpl.event.listener.LifeCycleListener;
import org.bigmouth.nvwa.dpl.status.Status;


public interface LifeCycle<C extends Config, L extends LifeCycleListener> {

	void init();

	void destroy();

	C getConfig();

	Status getStatus();

	Iterator<L> getAllEventListener();
}
