package org.bigmouth.nvwa.servicelogic.factory;

import java.util.List;

import org.bigmouth.nvwa.dpl.factory.AbsSpringStylePlugInFactory;
import org.bigmouth.nvwa.dpl.factory.PlugInFactory;
import org.bigmouth.nvwa.dpl.plugin.PlugIn;
import org.bigmouth.nvwa.utils.Transformer;
import org.springframework.context.ApplicationContext;

import com.google.common.collect.Lists;

public abstract class AbsServiceLogicPlugInFactory extends AbsSpringStylePlugInFactory implements
		PlugInFactory {

	private static final Transformer<List<ServiceDeployConfig>, PlugIn> DEFAULT_CONFIG2PLUGIN_TRANSFORMER = new DeployConfig2PlugInTransformer();

	public AbsServiceLogicPlugInFactory(ApplicationContext parent) {
		super(parent);
	}

	@Override
	public PlugIn create() {
		final List<ServiceDeployConfig> configs = getDeployConfigs();
		return getConfig2PlugInTransformer().transform(configs);
	}

	private List<ServiceDeployConfig> getDeployConfigs() {
		String[] configBeanNames = getDeployConfigBeanNames();
		if (null == configBeanNames || 0 == configBeanNames.length)
			throw new IllegalStateException("configBeanNames is blank.");
		final List<ServiceDeployConfig> configs = Lists.newArrayList();
		for (String configName : configBeanNames) {
			ServiceDeployConfig configBean = (ServiceDeployConfig) getContext().getBean(configName);
			configs.add(configBean);
		}
		return configs;
	}

	protected Transformer<List<ServiceDeployConfig>, PlugIn> getConfig2PlugInTransformer() {
		return DEFAULT_CONFIG2PLUGIN_TRANSFORMER;
	}

	@Override
	protected abstract String[] getContextFilePaths();

	protected abstract String[] getDeployConfigBeanNames();
}
