package org.bigmouth.nvwa.dpl.hotswap;

public interface ClassFilter {

	public static final ClassFilter DEFAULT = new ClassFilter() {

		@Override
		public boolean accept(Class<?> clazz) {
			return true;
		}
	};

	boolean accept(Class<?> clazz);
}
