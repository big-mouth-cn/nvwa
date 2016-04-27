package org.bigmouth.nvwa.dpl.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public abstract class AbsSpringStylePlugInFactory implements PlugInFactory {

	private final ApplicationContext context;

	public AbsSpringStylePlugInFactory(ApplicationContext parent) {
		if (null == parent)
			throw new NullPointerException("parentApplicationContext");

		String[] configFilePaths = getContextFilePaths();
		if (null == configFilePaths || 0 == configFilePaths.length)
			throw new IllegalStateException("getConfigFilePaths() is null.");
		context = new ClassPathXmlApplicationContext(configFilePaths, parent);
	}

	protected ApplicationContext getContext() {
		return context;
	}

	protected abstract String[] getContextFilePaths();
}
