package org.bigmouth.nvwa.dpl.hotswap;

public interface ResourceFilter {

	public static final ResourceFilter DEFAULT = new ResourceFilter() {

		@Override
		public boolean accept(String name) {
			return true;
		}
	};

	boolean accept(String name);
}
