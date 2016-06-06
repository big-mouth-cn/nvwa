package org.bigmouth.nvwa.dpl.service;

import org.bigmouth.nvwa.dpl.MutableLifeCycle;
import org.bigmouth.nvwa.dpl.event.listener.ServiceListener;
import org.bigmouth.nvwa.dpl.status.MutableStatus;

public interface MutableService extends Service, MutableLifeCycle<ServiceConfig, ServiceListener>,
		MutableStatus {

	void setAttachment(Object attachment);
}
