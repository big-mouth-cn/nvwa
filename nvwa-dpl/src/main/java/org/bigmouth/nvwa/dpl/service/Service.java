package org.bigmouth.nvwa.dpl.service;

import org.bigmouth.nvwa.dpl.ClassLoaderHolder;
import org.bigmouth.nvwa.dpl.LifeCycle;
import org.bigmouth.nvwa.dpl.event.listener.ServiceListener;
import org.bigmouth.nvwa.dpl.status.Status;
import org.bigmouth.nvwa.dpl.status.StatusSource;

public interface Service extends LifeCycle<ServiceConfig, ServiceListener>, Status, StatusSource,
		ServiceConfig, ClassLoaderHolder {

	ServiceStatistics getStatistics();

	/**
	 * e.g. extended info,extended statistics info.
	 * 
	 * @return
	 */
	Object getAttachment();
}
