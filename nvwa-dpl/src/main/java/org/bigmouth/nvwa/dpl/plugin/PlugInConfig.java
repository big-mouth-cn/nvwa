package org.bigmouth.nvwa.dpl.plugin;

import java.util.Iterator;

import org.bigmouth.nvwa.dpl.Config;
import org.bigmouth.nvwa.dpl.service.ServiceConfig;


public interface PlugInConfig extends Config {

	public Iterator<ServiceConfig> getServiceConfigs();
}
