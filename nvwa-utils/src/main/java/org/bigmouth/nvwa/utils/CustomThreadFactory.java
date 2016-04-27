package org.bigmouth.nvwa.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadFactory implements ThreadFactory {

	private static final AtomicInteger poolNumber = new AtomicInteger(1);

	private final ThreadGroup group;

	private final AtomicInteger threadNumber = new AtomicInteger(1);

	private final String namePrefix;

	private final ClassLoader threadContextClassLoader;

	public CustomThreadFactory(ThreadGroup group, String prefix,
			ClassLoader threadContextClassLoader) {
		if (group == null) {
			SecurityManager s = System.getSecurityManager();
			this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		} else {
			this.group = group;
		}
		if (prefix == null) {
			this.namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
		} else {
			this.namePrefix = prefix + "-" + poolNumber.getAndIncrement() + "-thread-";
		}
		this.threadContextClassLoader = threadContextClassLoader;
	}

	public CustomThreadFactory(String prefix, ClassLoader threadContextClassLoader) {
		this(null, prefix, threadContextClassLoader);
	}

	public CustomThreadFactory(String prefix) {
		this(null, prefix, null);
	}

	public CustomThreadFactory() {
		this(null, null, null);
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(),
				0);
		if (t.isDaemon())
			t.setDaemon(false);

		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);

		if (null != threadContextClassLoader)
			t.setContextClassLoader(threadContextClassLoader);

		return t;
	}
}
