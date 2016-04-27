package org.bigmouth.nvwa.dpl.factory;

public abstract class AbstractPlugInDiscover implements PlugInDiscover {

	private static final ResourceSearchSupport DEFAULT_FACTORY_SUPPORT = new ResourceSearchSupport();

	private final ResourceSearchSupport searchSupport;

	public AbstractPlugInDiscover() {
		this(DEFAULT_FACTORY_SUPPORT);
	}

	public AbstractPlugInDiscover(ResourceSearchSupport factorySupport) {
		if (null == factorySupport)
			throw new NullPointerException("factorySupport");
		this.searchSupport = factorySupport;
	}

	protected ResourceSearchSupport getSearchSupport() {
		return searchSupport;
	}
}
