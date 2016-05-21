package org.bigmouth.nvwa.dpl.factory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.dpl.hotswap.ClassFilter;
import org.bigmouth.nvwa.dpl.hotswap.PlugInClassLoader;
import org.bigmouth.nvwa.dpl.hotswap.ResourceFilter;
import org.bigmouth.nvwa.utils.JarFileUtils;

public class ResourceSearchSupport {

	private static final String PLUAGIN_CLASS_PATH = "org.bigmouth.nvwa.servicelogic.plugin.*";
	private static final String PLUAGIN_RESOURCE_PATH = "org/bigmouth/nvwa/servicelogic/plugin/*";
	private static final String METADATA_CFG_FILE_PATH = "plugin-metadata";
	private final String plugInBasePackage;
	private final String plugInBasePath;

	public ResourceSearchSupport() {
		this(PLUAGIN_CLASS_PATH, PLUAGIN_RESOURCE_PATH);
	}

	public ResourceSearchSupport(String plugInBasePackage, String plugInBasePath) {
		if (StringUtils.isBlank(plugInBasePackage))
			throw new IllegalArgumentException("plugInBasePackage is blank.");
		if (StringUtils.isBlank(plugInBasePath))
			throw new IllegalArgumentException("plugInBasePath is blank.");
		this.plugInBasePackage = plugInBasePackage;
		this.plugInBasePath = plugInBasePath;
	}

	public List<Class<?>> searchClasses(PlugInClassLoader pcl, ClassFilter classFilter) {
		return pcl.searchClasses(plugInBasePackage, classFilter);
	}

	public List<String> searchResources(PlugInClassLoader pcl, ResourceFilter resourceFilter,
			boolean isIncludeLib) {
		return pcl.searchResources(plugInBasePath, resourceFilter, isIncludeLib);
	}

	public List<Class<?>> searchClassesOf(PlugInClassLoader pcl, final Class<?> targetClass) {
		return pcl.searchClasses(plugInBasePackage, new ClassFilter() {

			@Override
			public boolean accept(Class<?> clazz) {
				return targetClass.isAssignableFrom(clazz);
			}
		});
	}

	@SuppressWarnings("unchecked")
	public Class<PlugInFactory> searchPlugInFactoryClass(String plugInPath, PlugInClassLoader pcl) {
		Class<PlugInFactory> factoryClass = null;
		byte[] metadata_content = null;
		try {
			metadata_content = JarFileUtils.getBytesFromJar(plugInPath, METADATA_CFG_FILE_PATH);
		} catch (Exception e) {
			// ignore
		}
		if (null == metadata_content) {
			List<Class<?>> factoryClassRet = searchClassesOf(pcl, PlugInFactory.class);
			if (null == factoryClassRet || 0 == factoryClassRet.size()) {
				return null;
				// throw new RuntimeException(
				// "Can not found plugin metadata file:["
				// + METADATA_CFG_FILE_PATH
				// +
				// "],or can not found any implementation class of PlugInFactory in classpath.");
			}
			factoryClass = (Class<PlugInFactory>) factoryClassRet.get(0);
		} else {
			Properties properties = new Properties();
			try {
				properties.load(new ByteArrayInputStream(metadata_content));
			} catch (IOException e) {
				throw new RuntimeException("Can not load metadata file.", e);
			}
			String plugInFactoryClassPath = (String) properties.get("PlugInFactory");

			try {
				factoryClass = (Class<PlugInFactory>) pcl.loadClass(plugInFactoryClassPath);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Can not load PlugInFactory class.", e);
			}
		}
		return factoryClass;
	}
}
