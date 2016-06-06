package org.bigmouth.nvwa.session;

import java.util.concurrent.ConcurrentMap;

public interface Session {

	String getId();

	boolean isNew();

	void setAttribute(String key, Object value);

	Object getAttribute(String key);
	
	void removeAttribute(String key);

	boolean existAttribute(String key);
	
	void setLastActiveTime(long lastActiveTime);
	
	void setNew(boolean isNew);
	
	void setAttrs(ConcurrentMap<String, Object> attrs);
	
	ConcurrentMap<String, Object> getAttrs();

	long getLastActiveTime();

	void destroy();
}
