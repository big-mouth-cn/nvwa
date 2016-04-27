package org.bigmouth.nvwa.dpl;

public interface ClassLoaderHolder {

	void setClassLoader(ClassLoader classloader);

	ClassLoader getClassLoader();
}
