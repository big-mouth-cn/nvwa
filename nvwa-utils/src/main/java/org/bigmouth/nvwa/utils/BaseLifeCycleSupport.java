package org.bigmouth.nvwa.utils;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public abstract class BaseLifeCycleSupport {

	private final ReentrantLock lock = new ReentrantLock();
	private final AtomicBoolean initialized = new AtomicBoolean(false);

	protected abstract void doInit();

	protected abstract void doDestroy();

	public void init() {
		if (!lock.tryLock()) {
			throw new RuntimeException("BaseLifeCycleSupport is processing for lifecycle.");
		}
		if (isInitialized()) {
			throw new RuntimeException("BaseLifeCycleSupport has been initialized.");
		}
		try {
			doInit();
		} finally {
			initialized.set(true);
			lock.unlock();
		}
	}

	protected boolean isInitialized() {
		return initialized.get();
	}

	public void destroy() {
		if (!lock.tryLock()) {
			throw new RuntimeException("BaseLifeCycleSupport is processing for lifecycle");
		}
		if (!isInitialized()) {
			throw new RuntimeException("BaseLifeCycleSupport has been destroyed.");
		}
		try {
			doDestroy();
		} finally {
			initialized.set(false);
			lock.unlock();
		}
	}
}
