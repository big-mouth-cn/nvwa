package org.bigmouth.nvwa.dpl;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.dpl.plugin.MutablePlugIn;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.bigmouth.nvwa.dpl.service.MutableService;
import org.bigmouth.nvwa.dpl.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultPlugInServiceBus implements PlugInServiceBus {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPlugInServiceBus.class);

	private final ConcurrentMap<String, PlugIn> plugInHolder = new ConcurrentHashMap<String, PlugIn>();

	@Override
	public void register(PlugIn plugIn) {
		if (null == plugIn)
			throw new NullPointerException("plugIn");
		String key = plugIn.getConfig().getKey();
		if (StringUtils.isBlank(key))
			throw new IllegalStateException("The plugIn's code is null or empty.");

		PlugIn _plugIn = plugInHolder.putIfAbsent(key, plugIn);
		if (null != _plugIn) {
			// TODO:
			throw new RuntimeException("plugIn[" + key + "] has existed,ignore.");
		} else {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("plugIn[{}] has registered successful.", key);
		}
	}

	@Override
	public void unregister(String plugInKey) {
		if (StringUtils.isBlank(plugInKey))
			throw new IllegalArgumentException("plugInKey is blank.");
		PlugIn _plugIn = plugInHolder.remove(plugInKey);
		if (null != _plugIn) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("plugIn[{}] has unregistered successful.", plugInKey);
		} else {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("no plugIn for " + plugInKey);
		}
	}

	@Override
	public Iterator<PlugIn> getAllPlugIns() {
		return plugInHolder.values().iterator();
	}

	@Override
	public PlugIn lookupPlugIn(String plugInKey) {
		if (StringUtils.isBlank(plugInKey))
			throw new IllegalArgumentException("plugInKey is blank.");
		PlugIn plugIn = plugInHolder.get(plugInKey);
		// if (null == plugIn)
		// throw new RuntimeException("can not found PlugIn for" + plugInKey);
		if (null == plugIn) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Can not found PlugIn for " + plugInKey);
		}
		return plugIn;
	}

	@Override
	public Service lookupService(String plugInKey, String serviceKey) {
		if (StringUtils.isBlank(plugInKey))
			throw new IllegalArgumentException("plugInKey is blank.");
		if (StringUtils.isBlank(serviceKey))
			throw new IllegalArgumentException("serviceKey is blank.");
		PlugIn plugIn = lookupPlugIn(plugInKey);
		if (null == plugIn) {
			// TODO:
			return null;
		} else {
			return plugIn.lookupService(serviceKey);
		}
	}

	@Override
	public void activePlugIn(String plugInKey) {
		MutablePlugIn plugIn = (MutablePlugIn) lookupPlugIn(plugInKey);
		plugIn.setRunning(true);
	}

	@Override
	public void activeService(String plugInKey, String serviceKey) {
		PlugIn plugIn = lookupPlugIn(plugInKey);
		MutableService service = (MutableService) plugIn.lookupService(serviceKey);
		service.setRunning(true);
	}

	@Override
	public void deactivePlugIn(String plugInKey) {
		MutablePlugIn plugIn = (MutablePlugIn) lookupPlugIn(plugInKey);
		plugIn.setRunning(false);
	}

	@Override
	public void deactiveService(String plugInKey, String serviceKey) {
		PlugIn plugIn = lookupPlugIn(plugInKey);
		MutableService service = (MutableService) plugIn.lookupService(serviceKey);
		service.setRunning(false);
	}
}
