package org.bigmouth.nvwa.utils.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DefaultFuture implements Future {

	/** A number of seconds to wait between two deadlock controls ( 5 seconds ) */
	private static final long DEAD_LOCK_CHECK_INTERVAL = 5000L;

	/** A lock used by the wait() method */
	private final Object lock;
	private FutureListener<?> firstListener;
	private List<FutureListener<?>> otherListeners;
	private Object result;
	private boolean ready;
	private int waiters;

	public DefaultFuture() {
		this.lock = this;
	}

	public Future await() throws InterruptedException {
		synchronized (lock) {
			while (!ready) {
				waiters++;
				try {
					// Wait for a notify, or if no notify is called,
					// assume that we have a deadlock and exit the
					// loop to check for a potential deadlock.
					lock.wait(DEAD_LOCK_CHECK_INTERVAL);
				} finally {
					waiters--;
					if (!ready) {
						checkDeadLock();
					}
				}
			}
		}
		return this;
	}

	public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
		return await(unit.toMillis(timeout));
	}

	public boolean await(long timeoutMillis) throws InterruptedException {
		return await0(timeoutMillis, true);
	}

	public Future awaitUninterruptibly() {
		try {
			await0(Long.MAX_VALUE, false);
		} catch (InterruptedException ie) {
			// Do nothing : this catch is just mandatory by contract
		}

		return this;
	}

	public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
		return awaitUninterruptibly(unit.toMillis(timeout));
	}

	public boolean awaitUninterruptibly(long timeoutMillis) {
		try {
			return await0(timeoutMillis, false);
		} catch (InterruptedException e) {
			throw new InternalError();
		}
	}

	/**
	 * Wait for the Future to be ready. If the requested delay is 0 or negative,
	 * this method immediately returns the value of the 'ready' flag. Every 5
	 * second, the wait will be suspended to be able to check if there is a
	 * deadlock or not.
	 * 
	 * @param timeoutMillis
	 *            The delay we will wait for the Future to be ready
	 * @param interruptable
	 *            Tells if the wait can be interrupted or not
	 * @return <code>true</code> if the Future is ready
	 * @throws InterruptedException
	 *             If the thread has been interrupted when it's not allowed.
	 */
	private boolean await0(long timeoutMillis, boolean interruptable) throws InterruptedException {
		long endTime = System.currentTimeMillis() + timeoutMillis;

		if (endTime < 0) {
			endTime = Long.MAX_VALUE;
		}

		synchronized (lock) {
			if (ready) {
				return ready;
			} else if (timeoutMillis <= 0) {
				return ready;
			}

			waiters++;

			try {
				for (;;) {
					try {
						long timeOut = Math.min(timeoutMillis, DEAD_LOCK_CHECK_INTERVAL);
						lock.wait(timeOut);
					} catch (InterruptedException e) {
						if (interruptable) {
							throw e;
						}
					}

					if (ready) {
						return true;
					}

					if (endTime < System.currentTimeMillis()) {
						return ready;
					}
				}
			} finally {
				waiters--;
				if (!ready) {
					checkDeadLock();
				}
			}
		}
	}

	private void checkDeadLock() {
		// TODO:checkDeadLock
	}

	public boolean isDone() {
		synchronized (lock) {
			return ready;
		}
	}

	/**
	 * Sets the result of the asynchronous operation, and mark it as finished.
	 */
	public void setValue(Object newValue) {
		synchronized (lock) {
			// Allow only once.
			if (ready) {
				return;
			}

			result = newValue;
			ready = true;
			if (waiters > 0) {
				lock.notifyAll();
			}
		}

		notifyListeners();
	}

	public Object getValue() {
		synchronized (lock) {
			return result;
		}
	}

	public Future addListener(FutureListener<?> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener");
		}

		boolean notifyNow = false;
		synchronized (lock) {
			if (ready) {
				notifyNow = true;
			} else {
				if (firstListener == null) {
					firstListener = listener;
				} else {
					if (otherListeners == null) {
						otherListeners = new ArrayList<FutureListener<?>>(1);
					}
					otherListeners.add(listener);
				}
			}
		}

		if (notifyNow) {
			notifyListener(listener);
		}
		return this;
	}

	public Future removeListener(FutureListener<?> listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener");
		}

		synchronized (lock) {
			if (!ready) {
				if (listener == firstListener) {
					if (otherListeners != null && !otherListeners.isEmpty()) {
						firstListener = otherListeners.remove(0);
					} else {
						firstListener = null;
					}
				} else if (otherListeners != null) {
					otherListeners.remove(listener);
				}
			}
		}

		return this;
	}

	private void notifyListeners() {
		// There won't be any visibility problem or concurrent modification
		// because 'ready' flag will be checked against both addListener and
		// removeListener calls.
		if (firstListener != null) {
			notifyListener(firstListener);
			firstListener = null;

			if (otherListeners != null) {
				for (FutureListener<?> l : otherListeners) {
					notifyListener(l);
				}
				otherListeners = null;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void notifyListener(FutureListener l) {
		try {
			l.operationComplete(this);
		} catch (Throwable t) {
			throw new RuntimeException("notifyListener:", t);
		}
	}
}
