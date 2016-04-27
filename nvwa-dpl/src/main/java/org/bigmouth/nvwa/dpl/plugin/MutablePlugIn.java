package org.bigmouth.nvwa.dpl.plugin;

import java.util.List;

import org.bigmouth.nvwa.dpl.MutableLifeCycle;
import org.bigmouth.nvwa.dpl.event.listener.PlugInListener;
import org.bigmouth.nvwa.dpl.service.Service;
import org.bigmouth.nvwa.dpl.status.MutableStatus;


public interface MutablePlugIn extends PlugIn, MutableLifeCycle<PlugInConfig, PlugInListener>,
		MutableStatus {

	void addService(Service service);

	void setServices(List<Service> services);

	void clearServices();

	void setInstallPath(String installPath);
}
