package org.bigmouth.nvwa.dpl.service;

import org.bigmouth.nvwa.dpl.ImmutableConfig;
import org.bigmouth.nvwa.dpl.plugin.PlugInConfig;

public class ImmutableServiceConfig extends ImmutableConfig implements ServiceConfig {

	public ImmutableServiceConfig(String name, String key, String desc) {
		super(name, key, desc);
	}

	@Override
	public PlugInConfig getPlugInConfig() {
		// TODO Auto-generated method stub
		return null;
	}
}
