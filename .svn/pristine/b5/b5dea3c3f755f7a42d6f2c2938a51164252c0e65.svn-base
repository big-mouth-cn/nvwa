package org.bigmouth.nvwa.session.xmc;

import net.rubyeye.xmemcached.MemcachedClient;

import org.bigmouth.nvwa.session.Session;
import org.bigmouth.nvwa.session.SessionHolder;
import org.bigmouth.nvwa.utils.StringHelper;

public class SimpleXmcSessionHolder extends DefaultXmcSessionHolder {

    private static final long serialVersionUID = 5614390966799316810L;

    public SimpleXmcSessionHolder(MemcachedClient memClient, int expire) {
		super(memClient, expire, null);
	}
	
	public SimpleXmcSessionHolder(MemcachedClient memClient, int expire, SessionHolder sessionHolder) {
        super(memClient, expire, sessionHolder);
    }

    @Override
	protected Session createSession() {
		return new SimpleXmcSession(StringHelper.uuid(), this);
	}
}
