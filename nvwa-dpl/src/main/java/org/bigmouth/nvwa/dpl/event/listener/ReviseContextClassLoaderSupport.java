package org.bigmouth.nvwa.dpl.event.listener;

import org.bigmouth.nvwa.dpl.ClassLoaderHolder;

public class ReviseContextClassLoaderSupport {

	private final ClassLoaderHolder classLoaderHolder;

	public ReviseContextClassLoaderSupport(ClassLoaderHolder classLoaderHolder) {
		super();
		if (null == classLoaderHolder)
			throw new NullPointerException("classLoaderHolder");
		this.classLoaderHolder = classLoaderHolder;
	}

	protected static interface ReviseCallback {
		void execute();
	}

	protected void revise(ReviseCallback closure) {
		ClassLoader alpha = Thread.currentThread().getContextClassLoader();
		ClassLoader beta = classLoaderHolder.getClassLoader();
		if (alpha != beta) {
			try {
				Thread.currentThread().setContextClassLoader(beta);
				closure.execute();
			} finally {
				Thread.currentThread().setContextClassLoader(alpha);
			}
		} else {
			closure.execute();
		}
	}
}
