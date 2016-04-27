package org.bigmouth.nvwa.dpl.status;

import java.util.List;

public interface StatusHolder {
	/**
	 * If savedStatus exists,then load it from store;<br>
	 * else save it to store.
	 * 
	 * @param source
	 * @throws Exception
	 */
	public void syncStatusOf(StatusSource source) throws Exception;

	/**
	 * save status to store.
	 * 
	 * @param source
	 * @throws Exception
	 */
	public void addOrUpdateStatusOf(StatusSource source) throws Exception;

	public Status getStatusOf(StatusSource source) throws Exception;

	public void removeStatusOf(StatusSource source) throws Exception;

	public List<Status> getAllStatus() throws Exception;
}
