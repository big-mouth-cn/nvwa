package org.bigmouth.nvwa.dpl.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringStylePlugInDiscover extends DefaultPlugInDiscover implements PlugInDiscover,
		ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringStylePlugInDiscover.class);
	private ApplicationContext applicationContext;

	public SpringStylePlugInDiscover() {
		super();
	}

	public SpringStylePlugInDiscover(ResourceSearchSupport factorySupport) {
		super(factorySupport);
	}

	@Override
	protected PlugInFactory createPlugInFactory(Class<PlugInFactory> factoryClass) {
		if (null == factoryClass) {
			if (LOGGER.isDebugEnabled())
				LOGGER
						.debug("SpringStylePlugInDiscover can !NOT! found any implementation of PlugInFactory,just ignore.");
			return null;
		}

		if (!isSpringStylePlugIn(factoryClass)) {
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("SpringStylePlugInDiscover !NOT! Spring style PlugIn,just ignore.");
			return null;
		}

		PlugInFactory plugInFactory = null;
		Constructor<PlugInFactory> c = null;
		try {
			c = factoryClass.getConstructor(ApplicationContext.class);
		} catch (SecurityException e) {
			throw new RuntimeException("Can not get factory's constructor.", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Can not get factory's constructor.", e);
		}

		try {
			plugInFactory = c.newInstance(applicationContext);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Can not instantiated PlugInFactory.", e);
		} catch (InstantiationException e) {
			throw new RuntimeException("Can not instantiated PlugInFactory.", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Can not instantiated PlugInFactory.", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Can not instantiated PlugInFactory.", e);
		}
		return plugInFactory;
	}

	private boolean isSpringStylePlugIn(Class<PlugInFactory> factoryClass) {
		return AbsSpringStylePlugInFactory.class.isAssignableFrom(factoryClass);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
