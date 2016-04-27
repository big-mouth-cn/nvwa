package org.bigmouth.nvwa.session;

public interface SessionHolder {

	/**
	 * 获取会话对象。
	 * 如果已经存在则直接返回，否则创建一个新的再返回。
	 * 
	 * @param trackable
	 * @return
	 */
	Session get(Trackable trackable);
	
	/**
	 * 获取会话对象。
	 * 如果已经存在则直接返回，否则返回为<code>null</code>。
	 * 
	 * @param trackable
	 * @return
	 */
	Session getSession(Trackable trackable);

	/**
	 * 保存一个会话。
	 * 
	 * @param session
	 */
	void put(Session session);

	/**
	 * 删除一个会话。
	 * 
	 * @param trackable
	 */
	void remove(Trackable trackable);
}
