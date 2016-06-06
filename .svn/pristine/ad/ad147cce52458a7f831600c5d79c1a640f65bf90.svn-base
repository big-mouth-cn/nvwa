package org.bigmouth.nvwa.dpl.event.listener;

import java.util.Iterator;

import org.bigmouth.nvwa.dpl.event.ActivedEvent;
import org.bigmouth.nvwa.dpl.event.ConfigChangedEvent;
import org.bigmouth.nvwa.dpl.event.CreatedEvent;
import org.bigmouth.nvwa.dpl.event.DeActivedEvent;
import org.bigmouth.nvwa.dpl.event.DestroyedEvent;
import org.bigmouth.nvwa.dpl.event.StatusChangedEvent;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.bigmouth.nvwa.dpl.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//TODO:
public class LoggingPlugInListener extends PlugInListenerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingPlugInListener.class);
	private static final String PLUGIN_SUFFIX = "PLUGIN";

	@Override
	public void init() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void onActived(ActivedEvent event) {
		super.onActived(event);
		PlugIn plugIn = (PlugIn) event.getSource();
		String servDesc = getServiceDesc(plugIn);
		if (LOGGER.isInfoEnabled())
			LOGGER.info(PLUGIN_SUFFIX + "[" + plugIn.getConfig().getKey() + "-" + servDesc
					+ "] has actived.");
	}

	@Override
	public void onConfigChanged(ConfigChangedEvent event) {
		super.onConfigChanged(event);
		PlugIn plugIn = (PlugIn) event.getSource();
		String servDesc = getServiceDesc(plugIn);
		if (LOGGER.isInfoEnabled())
			LOGGER.info(PLUGIN_SUFFIX + "[" + plugIn.getConfig().getKey() + "-" + servDesc
					+ "] 's config has changed.");
	}

	@Override
	public void onCreated(CreatedEvent event) {
		super.onCreated(event);
		PlugIn plugIn = (PlugIn) event.getSource();
		String servDesc = getServiceDesc(plugIn);
		if (LOGGER.isInfoEnabled())
			LOGGER.info(PLUGIN_SUFFIX + "[" + plugIn.getConfig().getKey() + "-" + servDesc
					+ "] has created.");
	}

	private String getServiceDesc(PlugIn plugIn) {
		StringBuilder sbServDesc = new StringBuilder(64);
		for (Iterator<Service> itr = plugIn.getAllServices(); itr.hasNext();) {
			sbServDesc.append(itr.next().getKey()).append(",");
		}
		String servDesc = "";
		if (sbServDesc.length() > 0)
			servDesc = sbServDesc.substring(0, sbServDesc.length() - 1);
		return servDesc;
	}

	@Override
	public void onDeActived(DeActivedEvent event) {
		super.onDeActived(event);
		PlugIn plugIn = (PlugIn) event.getSource();
		String servDesc = getServiceDesc(plugIn);
		if (LOGGER.isInfoEnabled())
			LOGGER.info(PLUGIN_SUFFIX + "[" + plugIn.getConfig().getKey() + "-" + servDesc
					+ "] has deactived.");
	}

	@Override
	public void onDestroyed(DestroyedEvent event) {
		super.onDestroyed(event);
		PlugIn plugIn = (PlugIn) event.getSource();
		String servDesc = getServiceDesc(plugIn);
		if (LOGGER.isInfoEnabled())
			LOGGER.info(PLUGIN_SUFFIX + "[" + plugIn.getConfig().getKey() + "-" + servDesc
					+ "] has destroyed.");
	}

	@Override
	public void onStatusChanged(StatusChangedEvent event) {
		super.onStatusChanged(event);
		PlugIn plugIn = (PlugIn) event.getSource();
		String servDesc = getServiceDesc(plugIn);
		if (LOGGER.isInfoEnabled())
			LOGGER.info(PLUGIN_SUFFIX + "[" + plugIn.getConfig().getKey() + "-" + servDesc
					+ "] 's status has changed,current status ["
					+ (plugIn.getStatus().isRunning() ? "active" : "deactive") + "]");
	}
}
