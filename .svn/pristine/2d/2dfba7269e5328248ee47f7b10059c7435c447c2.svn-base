package org.bigmouth.nvwa.session;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class DefaultSession implements MutableSession, Serializable {

	private static final long serialVersionUID = 6294354616416758926L;
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final String id;
	private volatile transient SessionHolder sessionHolder;

	private ConcurrentMap<String, Object> attrs = new ConcurrentHashMap<String, Object>();
	private volatile long lastActiveTime;
	private volatile boolean isNew = true;

    public DefaultSession(String id, SessionHolder sessionHolder) {
		if (StringUtils.isBlank(id))
			throw new IllegalArgumentException("id is blank");
		if (null == sessionHolder)
			throw new NullPointerException("sessionHolder");
		this.id = id;
		this.sessionHolder = sessionHolder;
		lastActiveTime = new Date().getTime();
	}

	@Override
	public void setAttribute(String key, Object value) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		attrs.put(key, value);
	}

	@Override
	public Object getAttribute(String key) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		return attrs.get(key);
	}

	@Override
	public boolean existAttribute(String key) {
		if (StringUtils.isBlank(key))
			throw new IllegalArgumentException("key is blank.");
		return attrs.containsKey(key);
	}

	@Override
    public void removeAttribute(String key) {
	    if (StringUtils.isBlank(key))
            throw new IllegalArgumentException("key is blank.");
	    attrs.remove(key);
    }

    @Override
	public String getId() {
		return id;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}

	@Override
	public long getLastActiveTime() {
		return lastActiveTime;
	}

	@Override
	public void update(long time) {
		this.lastActiveTime = time;
		isNew = false;
	}

	@Override
	public void setHolder(SessionHolder holder) {
		if (null == holder)
			throw new NullPointerException("holder");
		this.sessionHolder = holder;
	}

	protected SessionHolder getHolder() {
		return this.sessionHolder;
	}

	@Override
	public void destroy() {
		sessionHolder.remove(new Trackable() {

			@Override
			public String getTrackId() {
				return getId();
			}
		});
	}

	@Override
    public ConcurrentMap<String, Object> getAttrs() {
        return this.attrs;
    }
	
    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
    
    public void setAttrs(ConcurrentMap<String, Object> attrs) {
        this.attrs = attrs;
    }
    
    public void setSessionHolder(SessionHolder sessionHolder) {
        this.sessionHolder = sessionHolder;
    }

    @Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).append("attrs", attrs)
				.append("lastActiveTime", SDF.format(new Date(lastActiveTime))).toString();
	}
}
