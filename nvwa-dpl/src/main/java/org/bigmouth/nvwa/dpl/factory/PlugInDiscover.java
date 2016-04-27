package org.bigmouth.nvwa.dpl.factory;

import org.bigmouth.nvwa.dpl.hotswap.PlugInClassLoader;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;

public interface PlugInDiscover {

	PlugIn discover(PlugInClassLoader classloader);// TODO:define discover
	// exception?
}
