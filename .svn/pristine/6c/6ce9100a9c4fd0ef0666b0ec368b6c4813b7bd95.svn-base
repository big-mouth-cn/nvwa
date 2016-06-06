package org.bigmouth.nvwa.dpl.factory;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.springframework.context.ApplicationContext;


public abstract class GenericSpringStylePlugInFactory extends AbsSpringStylePlugInFactory {

	public GenericSpringStylePlugInFactory(ApplicationContext parent) {
		super(parent);
	}

	@Override
	public PlugIn create() {
		String plugInName = getPlugInBeanName();
		if (StringUtils.isBlank(plugInName))
			throw new IllegalStateException("getPlugInName return blank.");
		return (PlugIn) getContext().getBean(plugInName);
	}

	@Override
	protected abstract String[] getContextFilePaths();

	protected abstract String getPlugInBeanName();
}
