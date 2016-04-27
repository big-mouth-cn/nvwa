package org.bigmouth.nvwa.session.xmc;

import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.session.DefaultSession;
import org.bigmouth.nvwa.session.MutableSession;
import org.bigmouth.nvwa.session.Session;
import org.bigmouth.nvwa.session.SessionHolder;
import org.bigmouth.nvwa.session.Trackable;
import org.bigmouth.nvwa.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultXmcSessionHolder implements SessionHolder, Serializable {

    private static final long serialVersionUID = 6833227043742510558L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultXmcSessionHolder.class);
	private static final int DEFAULT_EXPIRE_TIME = 0;

	private final MemcachedClient memClient;
	private final int expireTime;
	
	private final SessionHolder sessionHolder;

	public DefaultXmcSessionHolder(MemcachedClient memClient) {
		this(memClient, DEFAULT_EXPIRE_TIME, null);
	}
	
	public DefaultXmcSessionHolder(MemcachedClient memClient, SessionHolder sessionHolder) {
	    this(memClient, DEFAULT_EXPIRE_TIME, sessionHolder);
    }

    public DefaultXmcSessionHolder(MemcachedClient memClient, int expire, SessionHolder sessionHolder) {
		if (null == memClient)
			throw new NullPointerException("memClient");
		if (expire < 0)
			throw new IllegalArgumentException("expire:" + expire);
		this.memClient = memClient;
		this.expireTime = expire;
		this.sessionHolder = sessionHolder;
	}

    protected MemcachedClient getMemClient() {
		return memClient;
	}

	protected int getExpireTime() {
		return expireTime;
	}

	protected Session createSession() {
		return new DefaultSession(StringHelper.uuid(), this);
	}

	@Override
	public Session get(Trackable trackable) {
		if (null == trackable)
			throw new NullPointerException("trackable");
		String trackId = trackable.getTrackId();
		Session ret = null;
		if (StringUtils.isBlank(trackId)) {
			ret = createSession();
			put(ret);
			return ret;
		}
		ret = getSessionFromMem(trackId, ret);
		if (null == ret) {
			ret = createSession();
			put(ret);
		} else {
			if (ret instanceof MutableSession) {
				((MutableSession) ret).setHolder(this);
			}
			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Get session from sessionHolder:" + ret);
		}
		return ret;
	}

	@Override
    public Session getSession(Trackable trackable) {
	    if (null == trackable)
            throw new NullPointerException("trackable");
        String trackId = trackable.getTrackId();
        Session ret = null;
        if (StringUtils.isBlank(trackId)) {
            throw new NullPointerException("trackable.id");
        }
        ret = getSessionFromMem(trackId, ret);
        if (ret != null) {
            if (ret instanceof MutableSession) {
                ((MutableSession) ret).setHolder(this);
            }
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Get session from sessionHolder:" + ret);
        }
        return ret;
    }

    private Session getSessionFromMem(final String trackId, Session ret) {
        Object obj = null;
		try {
			obj = memClient.get(trackId);
		} catch (TimeoutException e) {
		    LOGGER.error("get:", e);
		} catch (InterruptedException e) {
		    LOGGER.error("get:", e);
		} catch (MemcachedException e) {
		    LOGGER.error("get:", e);
		}
		if (obj == null) {
            obj = getStoreageSession(trackId, ret);
            if (null != obj) {
                ret = (Session) obj;
                put(ret);
            }
        }
        if (obj instanceof Session) {
            ret = (Session) obj;
            return ret;
        }
		return ret;
	}

    private Session getStoreageSession(final String trackId, Session ret) {
        // Get Session from Mongo
		if (null != sessionHolder) {
		    ret = sessionHolder.get(new Trackable() {
                private static final long serialVersionUID = 7169641477926867122L;

                @Override
                public String getTrackId() {
                    return trackId;
                }
            });
		}
        return ret;
    }

	@Override
	public void put(Session session) {
		if (null == session)
			throw new NullPointerException("session");
		try {
			memClient.set(session.getId(), getExpireTime(), session);
		} catch (TimeoutException e) {
			LOGGER.error("put:", e);
		} catch (InterruptedException e) {
            LOGGER.error("put:", e);
		} catch (MemcachedException e) {
            LOGGER.error("put:", e);
		}
		
		// Mongo put session
		if (null != sessionHolder)
		    sessionHolder.put(session);
		
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Put session to sessionHolder:" + session);
	}

	@Override
	public void remove(Trackable trackable) {
		if (null == trackable)
			throw new NullPointerException("trackable");
		String trackId = trackable.getTrackId();
		try {
			memClient.delete(trackId);
		} catch (TimeoutException e) {
		    LOGGER.error("remove:", e);
		} catch (InterruptedException e) {
		    LOGGER.error("remove:", e);
		} catch (MemcachedException e) {
		    LOGGER.error("remove:", e);
		}
		
		if (null != sessionHolder)
		    sessionHolder.remove(trackable);
		
		if (LOGGER.isDebugEnabled())
			LOGGER.debug("Remove session from sessionHolder,trackId:" + trackId);
	}
}
