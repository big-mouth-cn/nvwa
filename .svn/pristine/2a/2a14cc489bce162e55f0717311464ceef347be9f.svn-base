package org.bigmouth.nvwa.utils.concurrent;

import java.util.concurrent.TimeUnit;

public interface Future {

	/**
	 * Wait for the asynchronous operation to complete. The attached listeners
	 * will be notified when the operation is completed.
	 */
	Future await() throws InterruptedException;

	/**
	 * Wait for the asynchronous operation to complete with the specified
	 * timeout.
	 * 
	 * @return <tt>true</tt> if the operation is completed.
	 */
	boolean await(long timeout, TimeUnit unit) throws InterruptedException;

	/**
	 * Wait for the asynchronous operation to complete with the specified
	 * timeout.
	 * 
	 * @return <tt>true</tt> if the operation is completed.
	 */
	boolean await(long timeoutMillis) throws InterruptedException;

	/**
	 * Wait for the asynchronous operation to complete uninterruptibly. The
	 * attached listeners will be notified when the operation is completed.
	 * 
	 * @return the current Future
	 */
	Future awaitUninterruptibly();

	/**
	 * Wait for the asynchronous operation to complete with the specified
	 * timeout uninterruptibly.
	 * 
	 * @return <tt>true</tt> if the operation is completed.
	 */
	boolean awaitUninterruptibly(long timeout, TimeUnit unit);

	/**
	 * Wait for the asynchronous operation to complete with the specified
	 * timeout uninterruptibly.
	 * 
	 * @return <tt>true</tt> if the operation is finished.
	 */
	boolean awaitUninterruptibly(long timeoutMillis);

	/**
	 * Returns if the asynchronous operation is completed.
	 */
	boolean isDone();

	/**
	 * Returns the result of the asynchronous operation.
	 */
	Object getValue();

	/**
	 * TODO:
	 * 
	 * @param newValue
	 */
	void setValue(Object newValue);

	/**
	 * Adds an event <tt>listener</tt> which is notified when this future is
	 * completed. If the listener is added after the completion, the listener is
	 * directly notified.
	 */
	Future addListener(FutureListener<?> listener);

	/**
	 * Removes an existing event <tt>listener</tt> so it won't be notified when
	 * the future is completed.
	 */
	Future removeListener(FutureListener<?> listener);
}
