package org.bigmouth.nvwa.dpl.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.bigmouth.nvwa.dpl.hotswap.PlugInClassLoader;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultPlugInDiscover extends AbstractPlugInDiscover implements PlugInDiscover {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPlugInDiscover.class);

	public DefaultPlugInDiscover() {
		super();
	}

	public DefaultPlugInDiscover(ResourceSearchSupport factorySupport) {
		super(factorySupport);
	}

	@Override
	public PlugIn discover(PlugInClassLoader classloader) {
		if (null == classloader)
			throw new NullPointerException("classloader");

		Class<PlugInFactory> factoryClass = getSearchSupport().searchPlugInFactoryClass(
				classloader.getPlugInJarPath(), classloader);

		PlugInFactory plugInFactory = createPlugInFactory(factoryClass);
		if (null == plugInFactory)
			return null;
		return plugInFactory.create();
	}

	protected PlugInFactory createPlugInFactory(Class<PlugInFactory> factoryClass) {
		if (null == factoryClass) {
			if (LOGGER.isDebugEnabled())
				LOGGER
						.debug("DefaultPlugInDiscover can !NOT! found any implementation of PlugInFactory,just ignore.");
			return null;
		}

		PlugInFactory plugInFactory = null;
		Constructor<PlugInFactory> c = null;
		try {
			c = factoryClass.getConstructor();
		} catch (SecurityException e) {
			throw new RuntimeException("Can not get factory's constructor.", e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("Can not get factory's constructor.", e);
		}

		try {
			plugInFactory = c.newInstance();
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
}
