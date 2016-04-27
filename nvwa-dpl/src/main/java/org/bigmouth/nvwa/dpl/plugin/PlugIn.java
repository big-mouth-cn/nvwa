package org.bigmouth.nvwa.dpl.plugin;

import java.util.Iterator;

import org.bigmouth.nvwa.dpl.ClassLoaderHolder;
import org.bigmouth.nvwa.dpl.LifeCycle;
import org.bigmouth.nvwa.dpl.event.listener.PlugInListener;
import org.bigmouth.nvwa.dpl.service.Service;
import org.bigmouth.nvwa.dpl.status.Status;
import org.bigmouth.nvwa.dpl.status.StatusSource;


public interface PlugIn extends LifeCycle<PlugInConfig, PlugInListener>, Status, StatusSource,
		PlugInConfig, ClassLoaderHolder {

	Service lookupService(String serviceKey);

	Iterator<Service> getAllServices();

	String getInstallPath();
}
