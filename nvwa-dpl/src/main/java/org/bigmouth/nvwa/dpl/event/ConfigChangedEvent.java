package org.bigmouth.nvwa.dpl.event;

import org.bigmouth.nvwa.dpl.Config;

public class ConfigChangedEvent extends LifeCycleEvent {

	private static final long serialVersionUID = 1L;

	private final Config oldConfig;
	private final Config newConfig;

	public ConfigChangedEvent(Object source, Config oldConfig, Config newConfig) {
		super(source);
		if (null == oldConfig)
			throw new NullPointerException("oldConfig");
		if (null == newConfig)
			throw new NullPointerException("newConfig");
		this.oldConfig = oldConfig;
		this.newConfig = newConfig;
	}

	public Config getOldConfig() {
		return oldConfig;
	}

	public Config getNewConfig() {
		return newConfig;
	}
}
